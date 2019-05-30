package com.zjucsc.application.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.config.SocketIoEvent;
import com.zjucsc.application.config.StatisticsData;
import com.zjucsc.application.domain.bean.CollectorState;
import com.zjucsc.application.domain.bean.FvDimensionWrapper;
import com.zjucsc.application.domain.exceptions.DeviceNotValidException;
import com.zjucsc.application.domain.exceptions.ProtocolIdNotValidException;
import com.zjucsc.application.socketio.SocketServiceCenter;
import com.zjucsc.application.system.service.PacketAnalyzeService;
import com.zjucsc.application.system.service.iservice.CapturePacketService;
import com.zjucsc.application.tshark.capture.NewFvDimensionCallback;
import com.zjucsc.application.tshark.capture.ProcessCallback;
import com.zjucsc.application.tshark.domain.packet.ModbusPacket;
import com.zjucsc.application.tshark.domain.packet.S7CommPacket;
import com.zjucsc.application.tshark.domain.packet.UnknownPacket;
import com.zjucsc.application.tshark.handler.BadPacketAnalyzeHandler;
import com.zjucsc.application.tshark.pre_processor.S7CommPreProcessor;
import com.zjucsc.application.tshark.pre_processor.UnknownPreProcessor;
import com.zjucsc.application.util.CommonCacheUtil;
import com.zjucsc.application.util.CommonConfigUtil;
import com.zjucsc.application.util.PacketDecodeUtil;
import com.zjucsc.kafka.KafkaProducerCreator;
import com.zjucsc.tshark.handler.AbstractAsyncHandler;
import com.zjucsc.tshark.handler.DefaultPipeLine;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import com.zjucsc.tshark.pre_processor.BasePreProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.zjucsc.application.config.Common.COMMON_THREAD_EXCEPTION_HANDLER;
import static com.zjucsc.application.config.PACKET_PROTOCOL.MODBUS;
import static com.zjucsc.application.config.PACKET_PROTOCOL.S7;

@Slf4j
@Service
public class CapturePacketServiceImpl implements CapturePacketService<String,String> {

    @Autowired private PacketAnalyzeService packetAnalyzeService;

    private ListOperations<String,String> optForList;

    @Autowired
    public CapturePacketServiceImpl(RedisTemplate<String,String> redisTemplate){
        optForList = redisTemplate.opsForList();
    }

    private NewFvDimensionCallback newFvDimensionCallback;

    private List<BasePreProcessor> processorList = new ArrayList<>();
    private ProcessCallback<String,String> callback;

    private KafkaProducer<String,String> kafkaProducer = (KafkaProducer<String, String>) KafkaProducerCreator.getProducer("fv_dimension",
            String.class,String.class);

    //恶意报文handler
    private BadPacketAnalyzeHandler badPacketAnalyzeHandler = new BadPacketAnalyzeHandler(Executors.newFixedThreadPool(1,
            r -> {
                Thread thread = new Thread(r);
                thread.setName("bad_packet_analyze_handler-");
                thread.setUncaughtExceptionHandler(COMMON_THREAD_EXCEPTION_HANDLER);
                return thread;
            }));

    /**
     * 接收协议解析好的五元组，并作五元组发送、报文流量统计处理
     * 单线程处理，保证线程安全
     */
    public AbstractAsyncHandler<FvDimensionLayer> fvDimensionLayerAbstractAsyncHandler
            = new AbstractAsyncHandler<FvDimensionLayer>(Executors.newFixedThreadPool(1, r -> {
        Thread thread = new Thread(r);
        thread.setName("fv_dimension_handler_thread-");
        thread.setUncaughtExceptionHandler(COMMON_THREAD_EXCEPTION_HANDLER);
        return thread;
    })) {

        /**
         * 所有报文的入口方法
         * @param t 五元组
         * @return 返回的是设置了功能码的五元组，见setFuncode方法
         */
        @Override
        public FvDimensionLayer handle(Object t) {
            FvDimensionLayer fvDimensionLayer = ((FvDimensionLayer) t);
            //统计所有的IP地址
            if (fvDimensionLayer.ip_dst[0].length() > 0){
                CommonCacheUtil.statisticAllIpAddress(fvDimensionLayer.ip_dst[0]);
            }

            StringBuilder sb = stringBuilderThreadLocal.get();
            sb.delete(0,sb.length());
            //有些报文可能没有eth_trailer和eth_fcs
            if (fvDimensionLayer.eth_trailer[0].length() > 0) {
                sb.append(fvDimensionLayer.eth_trailer[0]).append(fvDimensionLayer.eth_fcs[0]);
            }
            //如果不存在，那么sb.toString==""，hexStringToByteArray2会自动判空，
            //然后返回EMPTY=""，即payload={}空的byte数组
            byte[] payload = PacketDecodeUtil.decodeTrailerAndFsc(sb.toString());
            //设置五元组中的功能码以及功能码对应的含义
            try {
                setFuncode(fvDimensionLayer);
            } catch (ProtocolIdNotValidException e) {
                log.error("error set Funcode" , e);
            }

            sendFvDimensionPacket(fvDimensionLayer , payload);          //发送五元组所有报文

            try {
                sendPacketStatisticsEvent(fvDimensionLayer);            //发送统计信息
            } catch (DeviceNotValidException e) {
                log.error("找不到IP地址对应的设备号 {} " , e.getMsg());
            }

            int collectorId = PacketDecodeUtil.decodeCollectorId(payload,24);
            analyzeCollectorState(payload , collectorId);                         //分析采集器状态信息
            int delay = collectorDelayInfo(payload , collectorId);                //解析时延信息

            //TODO TEST
            optForList.leftPush(CommonConfigUtil.getFvDimensionKeyInRedis(),
                    JSON.toJSONString(FvDimensionWrapper.builder()
                    .collectorId(collectorId)
                    .delay(delay)
                    .layer(fvDimensionLayer)
                    .build()));
            return fvDimensionLayer;                                              //将五元组发送给BadPacketHandler
        }
    };

    public void setFuncode(FvDimensionLayer layer) throws ProtocolIdNotValidException {
        if (!(layer instanceof UnknownPacket.LayersBean)) {
            int funCode = decodeFuncode(layer);
            if (funCode >= 0){
                layer.funCodeMeaning = CommonConfigUtil.
                        getTargetProtocolFuncodeMeanning(layer.frame_protocols[0],funCode);
                layer.funCode = String.valueOf(funCode);
            }else{
                layer.funCode = "--";
            }
        }
    }

    private int decodeFuncode(FvDimensionLayer t) {
        String funCodeStr;
        if (t instanceof S7CommPacket.LayersBean){
            funCodeStr =  ((S7CommPacket.LayersBean) t).s7comm_param_func[0];
            System.out.println("funcode str" + funCodeStr);
            return PacketDecodeUtil.decodeFuncode(S7, funCodeStr);
        }else if (t instanceof ModbusPacket.LayersBean){
            funCodeStr = ((ModbusPacket.LayersBean) t).modbus_func_code[0];
            return PacketDecodeUtil.decodeFuncode(MODBUS,funCodeStr);
        }else{
            //log.error("can not decode funCode of protocol : {} cause it is not defined" , t.frame_protocols[0]);
            return -1;
        }
    }

    @Async
    @Override
    public CompletableFuture<Exception> start(ProcessCallback<String,String> callback) {
        this.callback = callback;
        try {
            callback.start(doStart(fvDimensionLayerAbstractAsyncHandler
                                   //,new ModbusPreProcessor()
                                   ,new S7CommPreProcessor()
                                   //,new IEC104PreProcessor()
                                   ,new UnknownPreProcessor()      //必须放在解析协议的最后
                                   ));
        } catch (InterruptedException e) {
            return CompletableFuture.completedFuture(e);
        }
        return CompletableFuture.completedFuture(null);
    }

    private int collectorDelayInfo(byte[] payload , int collectorId) {
        if (payload.length > 0){
            if (collectorId > 0){
                //valid packet
                int collectorDelay = PacketDecodeUtil.decodeCollectorDelay(payload,4);
                //设置ID和延时用于发送
                //System.out.println("delay : " + collectorDelay);
                //packetAnalyzeService.setCollectorDelay(collectorId,collectorDelay);
                return collectorDelay;
            }
            return -1;
        }
        return -1;
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
            Common.NON_TIMESTAMP_PACKET_COUNT ++;
        }
        //SocketServiceCenter.updateAllClient(SocketIoEvent.ALL_PACKET,fvDimensionLayer);
        if (newFvDimensionCallback!=null){
            newFvDimensionCallback.newCome(fvDimensionLayer);
        }

    }

    private void sendPacketStatisticsEvent(FvDimensionLayer fvDimensionLayer) throws DeviceNotValidException {
        int capLength = Integer.parseInt(fvDimensionLayer.frame_cap_len[0]);

        StatisticsData.recvPacketNumber.addAndGet(capLength);      //总报文数
        //外界 --> PLC[ip_dst]  设备接收的报文数
        StatisticsData.increaseNumberByDeviceIn(CommonCacheUtil.getTargetDeviceNumberByIp(fvDimensionLayer.ip_dst[0])
            ,capLength);
        //外界 <-- PLC[ip_src]  设备发送的报文数
        StatisticsData.increaseNumberByDeviceOut(CommonCacheUtil.getTargetDeviceNumberByIp(fvDimensionLayer.ip_src[0]),
                capLength);
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
