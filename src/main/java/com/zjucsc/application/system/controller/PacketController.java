package com.zjucsc.application.system.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.zjucsc.application.domain.bean.NetworkInterface;
import com.zjucsc.application.socketio.SocketServiceCenter;
import com.zjucsc.application.system.service.impl.PacketServiceImpl;
import com.zjucsc.application.system.service.impl.TsharkMainService;
import com.zjucsc.base.BaseResponse;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.socketio.MainServer;
import com.zjucsc.application.domain.bean.CaptureService;
import com.zjucsc.application.domain.exceptions.OpenCaptureServiceException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.net.SocketException;
import java.util.List;
import java.util.concurrent.Callable;

import static com.zjucsc.application.config.Common.CAPTURE_COMMAND_MAC;
import static com.zjucsc.application.config.Common.HTTP_STATUS_CODE.SYS_ERROR;

@RestController
@RequestMapping("/service")
public class PacketController {

    @Qualifier("packet_service")
    @Autowired
    private PacketServiceImpl packetService;

    @Qualifier("tshark_main_service")
    @Autowired
    private TsharkMainService tsharkMainService;

    @ApiOperation(value="开始抓包")
    @RequestMapping(value = "/start_service" , method = RequestMethod.POST)
    public void startCaptureService(@RequestBody CaptureService service){
        doStartService(service);
    }

    /**
     * start packet-capture service
     * @return response
     */
    private void doStartService(CaptureService service){
        if (service == null) {
            if (Common.hasStartedHost.contains("default_service")){
                throw new OpenCaptureServiceException("local service has started");
            }
        }else{
            String inetAddress = service.getService_ip() + service.getPort();
            if (Common.hasStartedHost.contains(inetAddress)){
                throw new OpenCaptureServiceException(inetAddress + " service has started");
            }
        }
        tsharkMainService.start(CAPTURE_COMMAND_MAC);
    }

    @ApiOperation("连接websocket服务端交互")
    @RequestMapping(value = "/connect_socketio" , method = RequestMethod.GET)
    public BaseResponse startRecvRealTimePacket(){
        boolean b = MainServer.openWebSocketService("localhost", Common.SOCKET_IO_PORT, new com.corundumstudio.socketio.listener.ConnectListener() {
            @Override
            public void onConnect(SocketIOClient socketIOClient) {
                SocketServiceCenter.addConnectedClient(socketIOClient);
            }
        }, new com.corundumstudio.socketio.listener.DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient socketIOClient) {
                SocketServiceCenter.removeConnectedClient(socketIOClient);
            }
        });
        if (b){
            return BaseResponse.OK();
        }else{
            return BaseResponse.ERROR(SYS_ERROR,"服务已打开");
        }
    }

    @ApiOperation("获取抓包主机所有网卡接口信息")
    @GetMapping("get_all_interface")
    public List<NetworkInterface> getAllNetworkInterfaces() throws SocketException {
        return packetService.getAllNetworkInterface();
    }

    @ApiOperation("更新并获取抓包主机所有网卡接口信息")
    @GetMapping("get_all_interface_flush")
    public List<NetworkInterface> getAllNetworkInterfacesFlush() throws SocketException {
        return packetService.getAllNetworkInterfaceFlush();
    }
}
