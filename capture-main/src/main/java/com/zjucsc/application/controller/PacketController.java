package com.zjucsc.application.controller;

import com.zjucsc.application.config.Common;
import com.zjucsc.application.config.properties.ConstantConfig;
import com.zjucsc.application.config.auth.Log;
import com.zjucsc.application.domain.bean.BaseResponse;
import com.zjucsc.application.domain.bean.CaptureService;
import com.zjucsc.application.domain.bean.ServiceStatus;
import com.zjucsc.application.system.service.common_impl.NetworkInterfaceServiceImpl;
import com.zjucsc.application.system.service.common_iservice.CapturePacketService;
import com.zjucsc.application.system.service.common_iservice.IPacketService;
import com.zjucsc.application.tshark.capture.ProcessCallback;
import com.zjucsc.application.util.CacheUtil;
import com.zjucsc.common.exceptions.OpenCaptureServiceException;
import com.zjucsc.socket_io.MainServer;
import com.zjucsc.socket_io.SocketServiceCenter;
import com.zjucsc.tshark.pre_processor.BasePreProcessor;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    @Autowired private IPacketService iPacketService;

    @Log
    @ApiOperation(value="开始抓包")
    @RequestMapping(value = "/start_service" , method = RequestMethod.POST)
    public BaseResponse startCaptureService(@RequestBody CaptureService service) throws ExecutionException, InterruptedException {
        //开始周期任务
        CacheUtil.setScheduleServiceRunningState(true);
        return BaseResponse.OK(doStartService(service));
    }

    private final byte[] lock1 = new byte[]{};
    /**
     * start packet-capture service
     */

    @SuppressWarnings("unchecked")
    private Exception doStartService(CaptureService service) throws ExecutionException, InterruptedException {
        if (Common.systemRunType == 1) {
            synchronized (lock1){
                if (Common.hasStartedHost.contains(service.getService_name())){
                    throw new OpenCaptureServiceException(service.getService_name() + " service has started");
                }else{
                    Common.hasStartedHost.add(service.getService_name());
                }
            }
            service.macAddress = service.macAddress.replace("-" , ":");
            //real packet analyze
            //return oldServiceStart(service);
            return newServiceStart(service);
        }else{
            CompletableFuture<Exception> completeFuture = capturePacketService.startSimulate();
            Common.hasStartedHost.add("simulate");
            //simulate packet analyze
            try {
                return completeFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                return e;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Exception newServiceStart(CaptureService service) throws ExecutionException, InterruptedException {
        CompletableFuture<Exception> exceptionCompletableFuture = capturePacketService.newStart(service.getMacAddress(),service.getService_name());
        return exceptionCompletableFuture.get();
    }

    @SuppressWarnings("unchecked")
    private Exception oldServiceStart(CaptureService service) {
        BasePreProcessor.setCaptureDeviceNameAndMacAddress(service.macAddress , service.service_name);
        CompletableFuture<Exception> completableFuture = capturePacketService.start(new ProcessCallback<String, String>() {
            @Override
            public void error(Exception e) {

            }
            @Override
            public void start(String start) {
                if (log.isInfoEnabled()) {
                    log.info("{} has started capture service..", start);
                }
            }

            @Override
            public void end(String end) {
                if (log.isInfoEnabled()) {
                    log.info("{} has ended capture service..", end);
                }
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
    @ApiOperation("开启web-socket服务")
    @RequestMapping(value = "/connect_socketio" , method = RequestMethod.GET)
    public BaseResponse startRecvRealTimePacket(){
        boolean b = MainServer.openWebSocketService(constantConfig.getGlobal_address(), Common.SOCKET_IO_PORT, socketIOClient -> {
            if (socketIoClientNumber.get() >= 2){
                socketIOClient.disconnect();
                if (log.isInfoEnabled()) {
                    log.info("reject socket io connect : {} ", socketIOClient.getRemoteAddress().toString());
                }
            }else{
                socketIoClientNumber.addAndGet(1);
                if (log.isInfoEnabled()) {
                    log.info("[{}] connected...", socketIOClient.getRemoteAddress().toString());
                }
                SocketServiceCenter.addConnectedClient(socketIOClient);
            }
        }, socketIOClient -> {
            socketIoClientNumber.decrementAndGet();
            if (log.isInfoEnabled()) {
                log.info("[{}] disconnect...", socketIOClient.getRemoteAddress().toString());
            }
            SocketServiceCenter.removeConnectedClient(socketIOClient);
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
        return MainServer.close() ? BaseResponse.OK():BaseResponse.ERROR(500,"服务未打开");
    }

    @ApiOperation("获取抓包主机所有网卡接口信息")
    @GetMapping("get_all_interface")
    @Log
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
        CacheUtil.setScheduleServiceRunningState(false);
        if (Common.systemRunType == 1) {
            synchronized (lock1){
                if (!Common.hasStartedHost.contains(service_name)){
                    return BaseResponse.ERROR(500,service_name + " not open");
                }
                Common.hasStartedHost.remove(service_name);
            }
            log.info("close device : {} all opened device : {} " , service_name , Common.hasStartedHost);
            return BaseResponse.OK(capturePacketService.stop());
        }else{
            return BaseResponse.OK(capturePacketService.stopSimulate());
        }
    }

    @Log
    @ApiOperation("获取接口状态")
    @GetMapping("interface_status")
    public BaseResponse getInterfaceStatus(){
        int webSocketState = MainServer.getWebSocketServiceState()?1:0;
        return BaseResponse.OK(new ServiceStatus(webSocketState , Common.hasStartedHost.size() == 0 ? " " : Common.hasStartedHost.get(0)));
    }

    @ApiOperation("查询报文详细信息")
    @PostMapping("packet_detail")
    public BaseResponse getPacketDetail(@RequestBody String rawData) throws PcapNativeException, IOException, NotOpenException {
        String data = iPacketService.getPacketDetail(rawData);
        if (data == null){
            return BaseResponse.OK("解析超时，请重试");
        }
        return BaseResponse.OK(data);
    }
}
