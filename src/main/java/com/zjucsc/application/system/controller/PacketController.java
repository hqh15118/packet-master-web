package com.zjucsc.application.system.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.zjucsc.application.config.ConstantConfig;
import com.zjucsc.application.domain.exceptions.DeviceNotValidException;
import com.zjucsc.application.socketio.SocketServiceCenter;
import com.zjucsc.application.system.service.impl.PacketServiceImpl;
import com.zjucsc.application.tshark.capture.CapturePacketServiceImpl;
import com.zjucsc.application.tshark.capture.ProcessCallback;
import com.zjucsc.application.util.PcapUtils;
import com.zjucsc.base.BaseResponse;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.socketio.MainServer;
import com.zjucsc.application.domain.bean.CaptureService;
import com.zjucsc.application.domain.exceptions.OpenCaptureServiceException;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.net.SocketException;

import static com.zjucsc.application.config.Common.HTTP_STATUS_CODE.SYS_ERROR;

@Slf4j
@RestController
@RequestMapping("/service")
public class PacketController {

    @Qualifier("packet_service")
    @Autowired private PacketServiceImpl packetService;
    @Autowired private CapturePacketServiceImpl capturePacketService;
    @Autowired private ConstantConfig constantConfig;

    @ApiOperation(value="开始抓包")
    @RequestMapping(value = "/start_service" , method = RequestMethod.POST)
    public BaseResponse startCaptureService(@RequestBody CaptureService service) throws DeviceNotValidException {
        doStartService(service);
        return BaseResponse.OK();
    }

    private final byte[] lock1 = new byte[]{};
    /**
     * start packet-capture service
     */
    private void doStartService(CaptureService service) throws DeviceNotValidException {
        if(!service.getService_ip().equals(PcapUtils.getTargetNetworkInterfaceIp(service.getService_name()))){
            throw new DeviceNotValidException("设备名称与IP地址不符");
        }
        synchronized (lock1){
            if (Common.hasStartedHost.contains(service.getService_name())){
                throw new OpenCaptureServiceException(service.getService_name() + " service has started");
            }else{
                Common.hasStartedHost.add(service.getService_name());
            }
        }
        capturePacketService.start(new ProcessCallback<String, String>() {
            @Override
            public void error(Exception e) {

            }

            @Override
            public void start(String start) {
                log.info("{} has started capture service.." , start);
            }

            @Override
            public void end(String end) {
                log.info("{} has ended capture service.." , end);
            }
        });
    }

    @ApiOperation("开启websocket服务")
    @RequestMapping(value = "/connect_socketio" , method = RequestMethod.GET)
    public BaseResponse startRecvRealTimePacket(){
        boolean b = MainServer.openWebSocketService(constantConfig.getGlobal_address(), Common.SOCKET_IO_PORT, new com.corundumstudio.socketio.listener.ConnectListener() {
            @Override
            public void onConnect(SocketIOClient socketIOClient) {
                log.info(socketIOClient.getRemoteAddress().toString() + "connected...");
                SocketServiceCenter.addConnectedClient(socketIOClient);
            }
        }, new com.corundumstudio.socketio.listener.DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient socketIOClient) {
                log.info(socketIOClient.getRemoteAddress().toString() + "disconnect...");
                SocketServiceCenter.removeConnectedClient(socketIOClient);
            }
        });
        if (b){
            return BaseResponse.OK();
        }else{
            return BaseResponse.ERROR(SYS_ERROR,"服务已打开");
        }
    }

    @ApiOperation("关闭websocket服务")
    @GetMapping(value = "/close_socketio")
    public BaseResponse closeWebSocket(){
        return MainServer.close();
    }

    @ApiOperation("获取抓包主机所有网卡接口信息")
    @GetMapping("get_all_interface")
    public BaseResponse getAllNetworkInterfaces() throws SocketException {
        return BaseResponse.OK(packetService.getAllNetworkInterface());
    }

    @ApiOperation("更新并获取抓包主机所有网卡接口信息")
    @GetMapping("get_all_interface_flush")
    public BaseResponse getAllNetworkInterfacesFlush() throws SocketException {
        return BaseResponse.OK(packetService.getAllNetworkInterfaceFlush());
    }

    @ApiOperation("停止抓包")
    @PostMapping("stop_service")
    public BaseResponse stopService(@RequestBody CaptureService service){
        synchronized (lock1){
            if (!Common.hasStartedHost.contains(service.getService_name())){
                return BaseResponse.ERROR(500,service.getService_name() + " not open");
            }
            Common.hasStartedHost.remove(service.getService_name());
        }
        log.info("close device : {} all device : {} " , service.getService_name() , Common.hasStartedHost);
        capturePacketService.stop();
        return BaseResponse.OK();
    }
}
