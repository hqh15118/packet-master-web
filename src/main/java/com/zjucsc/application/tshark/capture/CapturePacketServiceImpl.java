package com.zjucsc.application.tshark.capture;

import com.zjucsc.application.config.SocketIoEvent;
import com.zjucsc.application.config.StatisticsData;
import com.zjucsc.application.domain.bean.CollectorState;
import com.zjucsc.application.domain.exceptions.DeviceNotValidException;
import com.zjucsc.application.handler.ThreadExceptionHandler;
import com.zjucsc.application.socketio.SocketServiceCenter;
import com.zjucsc.application.system.service.PacketAnalyzeService;
import com.zjucsc.application.tshark.decode.AbstractAsyncHandler;
import com.zjucsc.application.tshark.decode.DefaultPipeLine;
import com.zjucsc.application.tshark.domain.packet.FvDimensionLayer;
import com.zjucsc.application.tshark.handler.BadPacketAnalyzeHandler;
import com.zjucsc.application.tshark.pre_processor.*;
import com.zjucsc.application.util.CommonCacheUtil;
import com.zjucsc.application.util.PacketDecodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

import static com.zjucsc.application.config.Common.COMMON_THREAD_EXCEPTION_HANDLER;

@Slf4j
@Service
public class CapturePacketServiceImpl implements CapturePacketService<String,String>{

    @Autowired private PacketAnalyzeService packetAnalyzeService;
    private NewFvDimensionCallback newFvDimensionCallback;

    private List<BasePreProcessor> processorList = new ArrayList<>();
    private ProcessCallback<String,String> callback;
    //恶意报文handler
    private BadPacketAnalyzeHandler badPacketAnalyzeHandler = new BadPacketAnalyzeHandler(Executors.newFixedThreadPool(5,
            r -> {
                Thread thread = new Thread(r);
                thread.setName("bad_packet_analyze handler-");
                thread.setUncaughtExceptionHandler(COMMON_THREAD_EXCEPTION_HANDLER);
                return thread;
            }));

    /**
     * 接收协议解析好的五元组，并作五元组发送、报文流量统计处理
     * 单线程处理，保证线程安全
     */
    private AbstractAsyncHandler<FvDimensionLayer> fvDimensionLayerAbstractAsyncHandler
            = new AbstractAsyncHandler<FvDimensionLayer>(Executors.newFixedThreadPool(1, r -> {
        Thread thread = new Thread(r);
        thread.setName("fv_dimension_handler_thread-");
        thread.setUncaughtExceptionHandler(COMMON_THREAD_EXCEPTION_HANDLER);
        return thread;
    })) {
        @Override
        public FvDimensionLayer handle(Object t) {
            FvDimensionLayer fvDimensionLayer = ((FvDimensionLayer) t);
            StringBuilder sb = stringBuilderThreadLocal.get();
            sb.delete(0,sb.length());
            //有些报文可能没有eth_trailer和eth_fcs
            if (fvDimensionLayer.eth_trailer[0].length() > 0) {
                sb.append(fvDimensionLayer.eth_trailer[0]).append(fvDimensionLayer.eth_fcs[0]);
            }
            //如果不存在，那么sb.toString==""，hexStringToByteArray2会自动判空，
            //然后返回EMPTY=""，即payload={}空的byte数组
            byte[] payload = PacketDecodeUtil.decodeTrailerAndFsc(sb.toString());
            sendFvDimensionPacket(fvDimensionLayer , payload);          //发送五元组所有报文
            try {
                sendPacketStatisticsEvent(fvDimensionLayer);            //发送统计信息
            } catch (DeviceNotValidException e) {
                log.error("找不到IP地址对应的设备号" , e.getMsg());
            }
            int collectorId = PacketDecodeUtil.decodeCollectorId(payload,24);
            analyzeCollectorState(payload , collectorId);                         //分析采集器状态信息
            collectorDelayInfo(payload , collectorId);                            //解析时延信息
            return fvDimensionLayer;                                              //将五元组发送给BadPacketHandler
        }
    };

    @Async
    @Override
    public CompletableFuture<Exception> start(ProcessCallback<String,String> callback) {
        this.callback = callback;

        try {
            callback.start(doStart(fvDimensionLayerAbstractAsyncHandler ,
                                   new ModbusPreProcessor() ,
                                   new S7CommPreProcessor() ,
                                   new IEC104PreProcessor() ,
                                   new UnknownPreProcessor()      //必须放在解析协议的最后
                                   ));
        } catch (InterruptedException e) {
            return CompletableFuture.completedFuture(e);
        }
        return CompletableFuture.completedFuture(null);
    }

    private void collectorDelayInfo(byte[] payload , int collectorId) {
        if (payload.length > 0){
            if (collectorId > 0){
                //valid packet
                int collectorDelay = PacketDecodeUtil.decodeCollectorDelay(payload,4);
                //设置ID和延时用于发送
                //packetAnalyzeService.setCollectorDelay(collectorId,collectorDelay);
                //packetAnalyzeService.setCollectorDelay(collectorId,collectorDelay);
            }
        }
    }

    @Async
    @Override
    public CompletableFuture<Exception> stop() {
        for (BasePreProcessor basePreProcessor : processorList) {
            basePreProcessor.stopProcess();
            callback.end("end " + basePreProcessor.getClass().getName());
        }
        processorList.clear();
        return CompletableFuture.completedFuture(null);
    }

    private String doStart(AbstractAsyncHandler<FvDimensionLayer> fvDimensionHandler ,
                           BasePreProcessor... packetPreProcessor) throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(packetPreProcessor.length - 1);
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (; i < packetPreProcessor.length - 1; i++) {
            BasePreProcessor basePreProcessor = packetPreProcessor[i];
            doNow(basePreProcessor , fvDimensionHandler , downLatch , sb);
        }
        downLatch.await(100, TimeUnit.SECONDS);
        doNow(packetPreProcessor[i] , fvDimensionHandler,null , sb);
        return sb.toString();
    }

    private void doNow(BasePreProcessor basePreProcessor , AbstractAsyncHandler<FvDimensionLayer> fvDimensionHandler,
                       CountDownLatch downLatch , StringBuilder sb){
        String processName = basePreProcessor.getClass().getName();
        processorList.add(basePreProcessor);
        DefaultPipeLine pipeLine = new DefaultPipeLine(processName);
        //fv_dimension_handler --> bad_packet_analyze_handler
        pipeLine.addLast(fvDimensionHandler);
        pipeLine.addLast(badPacketAnalyzeHandler);
        basePreProcessor.setPipeLine(pipeLine);
        Thread processThread = new Thread(new Runnable() {
            @Override
            public void run() {
                basePreProcessor.setCommandBuildFinishCallback(()->{
                    if (downLatch!=null) {
                        downLatch.countDown();
                    }
                });
                basePreProcessor.execCommand(1 , -1);
            }
        });
        processThread.setName(processName + "-thread");
        processThread.start();
        sb.append(basePreProcessor.getClass().getName()).append(" start");
    }

    private static ThreadLocal<StringBuilder> stringBuilderThreadLocal =
            new ThreadLocal<StringBuilder>(){
                @Override
                protected StringBuilder initialValue() {
                    return new StringBuilder(50);
                }
            };

    private void sendFvDimensionPacket(FvDimensionLayer fvDimensionLayer , byte[] payload){
        fvDimensionLayer.timeStamp = PacketDecodeUtil.decodeTimeStamp(payload,20);
        //payload如果是空的，那么timeStamp就用本地时间来代替
        if (payload.length == 0){
            log.error("fv dimension layer is : {} 没有trailer和fcs，无法解析时间戳，返回上位机系统时间" , fvDimensionLayer);
        }
        //System.out.println(fvDimensionLayer);
        //SocketServiceCenter.updateAllClient(SocketIoEvent.ALL_PACKET,fvDimensionLayer);
        if (newFvDimensionCallback!=null){
            newFvDimensionCallback.newCome(fvDimensionLayer);
        }
    }

    private void sendPacketStatisticsEvent(FvDimensionLayer fvDimensionLayer) throws DeviceNotValidException {
        StatisticsData.recvPacketNumber.incrementAndGet();      //总报文数
        //外界 --> PLC[ip_dst]  设备接收的报文数
        StatisticsData.increaseNumberByDeviceIn(CommonCacheUtil.getTargetDeviceNumberByIp(fvDimensionLayer.ip_dst[0]));
        //外界 <-- PLC[ip_src]  设备发送的报文数
        StatisticsData.increaseNumberByDeviceOut(CommonCacheUtil.getTargetDeviceNumberByIp(fvDimensionLayer.ip_src[0]));
    }

    private void analyzeCollectorState(byte[] payload , int collectorId){
        CollectorState collectorState = PacketDecodeUtil.decodeCollectorState(payload,24,collectorId);
        if (collectorState!=null){
            log.info("**********************\ncollector state change : {} \n **********************" , collectorState);
            SocketServiceCenter.updateAllClient(SocketIoEvent.COLLECTOR_STATE,collectorState);
        }
        //System.out.println(collectorId + " -- " + collectorState);
    }

    public void setNewFvDimensionCallback(NewFvDimensionCallback newFvDimensionCallback){
        this.newFvDimensionCallback = newFvDimensionCallback;
    }
}
