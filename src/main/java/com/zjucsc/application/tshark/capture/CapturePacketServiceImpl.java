package com.zjucsc.application.tshark.capture;

import com.zjucsc.application.config.Common;
import com.zjucsc.application.config.SocketIoEvent;
import com.zjucsc.application.domain.bean.CollectorState;
import com.zjucsc.application.handler.ThreadExceptionHandler;
import com.zjucsc.application.socketio.SocketServiceCenter;
import com.zjucsc.application.system.service.PacketAnalyzeService;
import com.zjucsc.application.tshark.decode.AbstractAsyncHandler;
import com.zjucsc.application.tshark.decode.DefaultPipeLine;
import com.zjucsc.application.tshark.domain.packet.FvDimensionLayer;
import com.zjucsc.application.tshark.handler.BadPacketAnalyzeHandler;
import com.zjucsc.application.tshark.pre_processor.*;
import com.zjucsc.application.util.PacketDecodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class CapturePacketServiceImpl implements CapturePacketService<String,String>{

    @Autowired private PacketAnalyzeService packetAnalyzeService;

    private List<BasePreProcessor<?>> processorList = new ArrayList<>();
    private ProcessCallback<String,String> callback;
    private BadPacketAnalyzeHandler badPacketAnalyzeHandler = new BadPacketAnalyzeHandler(Executors.newFixedThreadPool(5,
            r -> {
                Thread thread = new Thread(r);
                thread.setName("bad packet analyze handler-");
                thread.setUncaughtExceptionHandler(new ThreadExceptionHandler());
                return thread;
            }));

    @Override
    public void start(ProcessCallback<String,String> callback) {
        this.callback = callback;
        /**
         * 接收协议解析好的五元组，并作五元组发送、报文流量统计处理
         */
        AbstractAsyncHandler<FvDimensionLayer> fvDimensionLayerAbstractAsyncHandler
                = new AbstractAsyncHandler<FvDimensionLayer>(Executors.newFixedThreadPool(10, r -> {
                    Thread thread = new Thread(r);
                    thread.setName("fv_dimension_handler_thread");
                    return thread;
                })) {
            @Override
            public FvDimensionLayer handle(Object t) {
                FvDimensionLayer fvDimensionLayer = ((FvDimensionLayer) t);
                sb.delete(0,50);
                //有些报文可能没有eth_trailer和eth_fcs
                sb.append(fvDimensionLayer.eth_trailer[0]).append(fvDimensionLayer.eth_fcs[0],2,10);
                //如果不存在，那么sb.toString==""，hexStringToByteArray2会自动判空，
                //然后返回EMPTY=""，即payload={}空的byte数组
                byte[] payload = PacketDecodeUtil.hexStringToByteArray2(sb.toString());
                sendFvDimensionPacket(fvDimensionLayer , payload);      //发送五元组所有报文
                sendPacketStatisticsEvent(fvDimensionLayer);            //发送统计信息
                analyzeCollectorState(payload);                         //分析采集器状态信息
                collectorDelayInfo(payload);                            //解析时延信息
                return fvDimensionLayer;                                //将五元组发送给BadPacketHandler
            }
        };

        callback.start(doStart(fvDimensionLayerAbstractAsyncHandler , new ModbusPreProcessor() ,
                               new S7CommPreProcessor() ,
                               new IEC104PreProcessor() ,

                               new UnknownPreProcessor()      //必须放在最后
                               ));
    }

    private void collectorDelayInfo(byte[] payload) {
        int collectorId = PacketDecodeUtil.decodeCollectorId(payload,24);
        if (collectorId > 0){
            //valid packet
            long collectorDelay = PacketDecodeUtil.decodeCollectorDelay(payload,4);
            packetAnalyzeService.setCollectorDelay(collectorId,collectorDelay);
        }
    }

    @Override
    public void stop() {
        for (BasePreProcessor<?> basePreProcessor : processorList) {
            basePreProcessor.stopProcess();
            callback.end("end " + basePreProcessor.getClass().getName());
        }
    }

    private String doStart(AbstractAsyncHandler<FvDimensionLayer> fvDimensionHandler ,
                           BasePreProcessor<?>... packetPreProcessor){
        StringBuilder sb = new StringBuilder();
        for (BasePreProcessor<?> basePreProcessor : packetPreProcessor) {
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
                    basePreProcessor.execCommand(0 , 10 , null);
                }
            });
            processThread.setName(processName + "-thread");
            processThread.start();
            sb.append(basePreProcessor.getClass().getName()).append(" start");
        }
        return sb.toString();
    }

    private StringBuilder sb = new StringBuilder(50);

    private void sendFvDimensionPacket(FvDimensionLayer fvDimensionLayer , byte[] payload){
        //payload如果是空的，那么timeStamp就用本地时间来代替
        if (payload.length == 0){
            log.error("{} 没有trailer和fcs，无法解析时间戳，返回上位机系统时间" , fvDimensionLayer);
        }
        fvDimensionLayer.timeStamp = PacketDecodeUtil.decodeTimeStamp(payload,20);
        SocketServiceCenter.updateAllClient(SocketIoEvent.ALL_PACKET,fvDimensionLayer);
    }

    private void sendPacketStatisticsEvent(FvDimensionLayer fvDimensionLayer){
        Common.recvPacketNuber += 1;
        Common.recvPacketFlow += Integer.parseInt(fvDimensionLayer.frame_cap_len[0]);
    }

    private void analyzeCollectorState(byte[] payload){
        CollectorState collectorState = PacketDecodeUtil.decodeCollectorState(payload,24);
        if (collectorState!=null){
            SocketServiceCenter.updateAllClient(SocketIoEvent.COLLECTOR_STATE,collectorState);
        }
    }
}
