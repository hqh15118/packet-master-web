package com.zjucsc.application.tshark.handler;

import com.zjucsc.application.config.SocketIoEvent;
import com.zjucsc.application.domain.exceptions.ProtocolIdNotValidException;
import com.zjucsc.application.socketio.SocketServiceCenter;
import com.zjucsc.application.system.entity.FvDimensionFilter;
import com.zjucsc.application.tshark.analyzer.FiveDimensionAnalyzer;
import com.zjucsc.application.tshark.analyzer.OperationAnalyzer;
import com.zjucsc.application.tshark.decode.AbstractAsyncHandler;
import com.zjucsc.application.tshark.domain.bean.BadPacket;
import com.zjucsc.application.tshark.domain.packet.FvDimensionLayer;
import com.zjucsc.application.tshark.domain.packet.ModbusPacket;
import com.zjucsc.application.tshark.domain.packet.S7CommPacket;
import com.zjucsc.application.tshark.domain.packet.UnknownPacket;
import com.zjucsc.application.util.PacketDecodeUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import static com.zjucsc.application.config.Common.FV_DIMENSION_FILTER_PRO;
import static com.zjucsc.application.config.Common.OPERATION_FILTER_PRO;
import static com.zjucsc.application.config.PACKET_PROTOCOL.MODBUS;
import static com.zjucsc.application.config.PACKET_PROTOCOL.S7;

@Slf4j
public class BadPacketAnalyzeHandler extends AbstractAsyncHandler<Void> {

    public BadPacketAnalyzeHandler(ExecutorService executor) {
        super(executor);
    }

    @Override
    public Void handle(Object t) {
        FvDimensionLayer layer = ((FvDimensionLayer) t);
        //五元组分析
        protocolAnalyze(layer);
        //功能码分析
        //TODO 只对确定有功能码的报文进行分析，其他的不需要作功能码分析
        if (!(t instanceof UnknownPacket.LayersBean)) {
            int funCode = decodeFuncode(t);
            if (funCode!=0) {
                operationAnalyze(funCode, layer);
            }
        }
        return null;
    }

    private int decodeFuncode(Object t) {
        String funCodeStr;
        if (t instanceof S7CommPacket.LayersBean){
            funCodeStr =  ((S7CommPacket.LayersBean) t).s7comm_param_func[0];
            return PacketDecodeUtil.decodeFuncode(S7,funCodeStr);
        }else if (t instanceof ModbusPacket.LayersBean){
            funCodeStr = ((ModbusPacket.LayersBean) t).modbus_func_code[0];
            return PacketDecodeUtil.decodeFuncode(MODBUS,funCodeStr);
        }else{
            log.error("can not decode funCode of {} cause it is not defined" , t);
            return 0;
        }
    }

    private void artifactAnalyze(byte[] tcpPayload, FvDimensionFilter packet) {
        /*
         * 只有定义了工艺参数分析器的报文才需要分析，其他都不需要直接略过
         */

    }

    private ThreadLocal<AnalyzerConsumerForOperation> analyzerThreadlocalForOperation
            = ThreadLocal.withInitial(AnalyzerConsumerForOperation::new);

    private ThreadLocal<AnalyzerConsumerForFVDimension> analyzerThreadlocalForFvDimension
            = ThreadLocal.withInitial(AnalyzerConsumerForFVDimension::new);

    private void protocolAnalyze(FvDimensionLayer layer) {
        /*
         * 调用五元组分析器进行解析【所有报文都需要进行五元组分析】
         */
        FV_DIMENSION_FILTER_PRO.forEach(analyzerThreadlocalForFvDimension.get().setPacketWrapper(layer));
    }

    private void operationAnalyze(int funCode , FvDimensionLayer layer){
        //analyzerThreadlocalForOperation.get() --> AnalyzerConsumerForOperation
        OPERATION_FILTER_PRO.forEach(analyzerThreadlocalForOperation.get().setPacketWrapper(layer,funCode));
    }

    private static class AnalyzerConsumerForOperation extends AbstractAnalyzerConsumer implements BiConsumer<String, ConcurrentHashMap<String, OperationAnalyzer>>{

        private FvDimensionLayer layer;
        private int funCode;

        @Override                                               //String是协议，OperationAnalyzer是对应的报文规则器
        public void accept(String deviceId, ConcurrentHashMap<String, OperationAnalyzer> stringOperationAnalyzerConcurrentHashMap) {
            OperationAnalyzer operationAnalyzer = null;
            /**
             * 只有定义了分析器的报文[即配置了过滤规则的]才需要功能码解析，其他的报文直接略过
             * layer.frame_protocols[0] 这个协议已经被统一过了
             */
            if ((operationAnalyzer = stringOperationAnalyzerConcurrentHashMap.get(layer.frame_protocols[0]))!=null){
                BadPacket badPacket = null;

                try {
                    badPacket = (BadPacket) operationAnalyzer.analyze(funCode,layer);
                } catch (ProtocolIdNotValidException e) {
                    log.error(" " , e);
                }
                if (badPacket!=null){
                    badPacket.setDeviceId(deviceId);
                    sendBadPacket(badPacket);
                }
            }else{
                //log.error("********************* \n not define {} 's operation analyzer , please check . \n *********************");
            }
        }

        public AnalyzerConsumerForOperation setPacketWrapper(FvDimensionLayer layer , int fun_code){
            this.layer = layer;
            this.funCode = fun_code;
            return this;
        }
    }

    private static class AnalyzerConsumerForFVDimension extends AbstractAnalyzerConsumer implements BiConsumer<String, FiveDimensionAnalyzer>{

        private FvDimensionLayer fvDimensionLayer;
        public AnalyzerConsumerForFVDimension setPacketWrapper(FvDimensionLayer fvDimensionLayer){
            this.fvDimensionLayer = fvDimensionLayer;
            return this;
        }
        @Override
        public void accept(String integer, FiveDimensionAnalyzer fiveDimensionAnalyzer) {
            BadPacket badPacket = ((BadPacket)fiveDimensionAnalyzer.analyze(fvDimensionLayer));
            if (badPacket!=null){
                sendBadPacket(badPacket);
            }
        }
    }

    private static class AbstractAnalyzerConsumer {

        public static Executor sendBadPacketThreadPool = Executors.newSingleThreadExecutor(
                r -> {
                    Thread thread = new Thread(r);
                    thread.setName("-BadPacketAnalyzeHandler-");
                    return thread;
                }
        );

        protected void sendBadPacket(BadPacket badPacket) {

            sendBadPacketThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("send bad packet : " + badPacket);
                    System.out.println("**********************");
                    SocketServiceCenter.updateAllClient(SocketIoEvent.BAD_PACKET,badPacket);
                }
            });
        }
    }
}
