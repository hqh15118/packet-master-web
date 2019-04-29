package com.zjucsc.application.system.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.zjucsc.application.domain.bean.NetworkInterface;
import com.zjucsc.application.system.service.impl.PacketServiceImpl;
import com.zjucsc.base.BaseResponse;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.socketio.MainServer;
import com.zjucsc.application.domain.bean.CaptureService;
import com.zjucsc.application.domain.exceptions.OpenCaptureServiceException;
import com.zjucsc.application.tshark.PacketService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.net.SocketException;
import java.util.List;

@RestController
@RequestMapping("/service")
public class PacketController {

    @Qualifier("packet_service")
    @Autowired
    private PacketServiceImpl packetService;

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
        PacketService.openPacketService(Common.CAPTURE_COMMAND);
    }

    @ApiOperation("连接websocket服务端交互")
    @RequestMapping(value = "/connect_socketio" , method = RequestMethod.GET)
    public BaseResponse startRecvRealTimePacket(){
        boolean b = MainServer.openWebSocketService("locahost", Common.SOCKET_IO_PORT, new com.corundumstudio.socketio.listener.ConnectListener() {
            @Override
            public void onConnect(SocketIOClient socketIOClient) {
                Common.addConnectedClient(socketIOClient);
            }
        }, new com.corundumstudio.socketio.listener.DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient socketIOClient) {
                Common.removeConnectedClient(socketIOClient);
            }
        });
        if (b){
            return BaseResponse.OK();
        }else{
            return BaseResponse.ERROR(503,"服务已打开");
        }
    }


    @GetMapping("getallinterface")
    public List<NetworkInterface> getAllNetworkInterfaces() throws SocketException {
        return packetService.getAllNetworkInterface();
    }

}
