package com.zjucsc.application.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.zjucsc.application.config.ConstantConfig;
import com.zjucsc.application.config.auth.Log;
import com.zjucsc.application.domain.bean.ServiceStatus;
import com.zjucsc.application.socketio.SocketServiceCenter;
import com.zjucsc.application.system.service.common_iservice.CapturePacketService;
import com.zjucsc.application.system.service.common_impl.NetworkInterfaceServiceImpl;
import com.zjucsc.application.tshark.capture.ProcessCallback;
import com.zjucsc.base.BaseResponse;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.socketio.MainServer;
import com.zjucsc.application.domain.bean.CaptureService;
import com.zjucsc.application.domain.exceptions.OpenCaptureServiceException;
import com.zjucsc.tshark.pre_processor.BasePreProcessor;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.SocketException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import static com.zjucsc.application.config.Common.HTTP_STATUS_CODE.SYS_ERROR;

@Slf4j
@RestController
@RequestMapping("/service")
public class PacketController {

    @Autowired private NetworkInterfaceServiceImpl networkInterfaceService;
    @Autowired private CapturePacketService capturePacketService;
    @Autowired private ConstantConfig constantConfig;

    @Log
    @ApiOperation(value="开始抓包")
    @RequestMapping(value = "/start_service" , method = RequestMethod.POST)
    public BaseResponse startCaptureService(@RequestBody CaptureService service) {
        return BaseResponse.OK(doStartService(service));
    }

    private final byte[] lock1 = new byte[]{};
    /**
     * start packet-capture service
     */
    @SuppressWarnings("unchecked")
    private Exception doStartService(CaptureService service) {
        synchronized (lock1){
            if (Common.hasStartedHost.contains(service.getService_name())){
                throw new OpenCaptureServiceException(service.getService_name() + " service has started");
            }else{
                Common.hasStartedHost.add(service.getService_name());
            }
        }
        service.macAddress = service.macAddress.replace("-" , ":");
        BasePreProcessor.setCaptureDeviceNameAndMacAddress(service.macAddress , service.service_name);

        CompletableFuture<Exception> completableFuture = capturePacketService.start(new ProcessCallback<String, String>() {
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

        try {
            return completableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            return e;
        }
    }

    private AtomicInteger socketIoClientNumber = new AtomicInteger(0);

    @Log
    @ApiOperation("开启websocket服务")
    @RequestMapping(value = "/connect_socketio" , method = RequestMethod.GET)
    public BaseResponse startRecvRealTimePacket(){
        boolean b = MainServer.openWebSocketService(constantConfig.getGlobal_address(), Common.SOCKET_IO_PORT, new com.corundumstudio.socketio.listener.ConnectListener() {
            @Override
            public void onConnect(SocketIOClient socketIOClient) {
                if (socketIoClientNumber.get() >= 1){
                    socketIOClient.disconnect();
                    log.info("reject socket io connect : {} " , socketIOClient.getRemoteAddress().toString());
                }else{
                    log.info(socketIOClient.getRemoteAddress().toString() + "connected...");
                    SocketServiceCenter.addConnectedClient(socketIOClient);
                }
            }
        }, new com.corundumstudio.socketio.listener.DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient socketIOClient) {
                socketIoClientNumber.decrementAndGet();
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

    @Log
    @ApiOperation("关闭websocket服务")
    @GetMapping(value = "/close_socketio")
    public BaseResponse closeWebSocket(){
        return MainServer.close();
    }

    @ApiOperation("获取抓包主机所有网卡接口信息")
    @GetMapping("get_all_interface")
    public BaseResponse getAllNetworkInterfaces() throws SocketException {
        return BaseResponse.OK(networkInterfaceService.getAllNetworkInterface());
    }

    @Log
    @ApiOperation("更新并获取抓包主机所有网卡接口信息")
    @GetMapping("get_all_interface_flush")
    public BaseResponse getAllNetworkInterfacesFlush() throws SocketException {
        return BaseResponse.OK(networkInterfaceService.getAllNetworkInterfaceFlush());
    }

    @Log
    @ApiOperation("停止抓包")
    @GetMapping("stop_service")
    public BaseResponse stopService(@RequestParam String service_name){
        synchronized (lock1){
            if (!Common.hasStartedHost.contains(service_name)){
                return BaseResponse.ERROR(500,service_name + " not open");
            }
            Common.hasStartedHost.remove(service_name);
        }
        log.info("close device : {} all opened device : {} " , service_name , Common.hasStartedHost);
        capturePacketService.stop();
        return BaseResponse.OK();
    }

    @Log
    @ApiOperation("获取接口状态")
    @GetMapping("interface_status")
    public BaseResponse getInterfaceStatus(){
        int webSocketState = MainServer.getWebSocketServiceState()?1:0;
        return BaseResponse.OK(new ServiceStatus(webSocketState , Common.hasStartedHost.size() == 0 ? " " : Common.hasStartedHost.get(0)));
    }
}
