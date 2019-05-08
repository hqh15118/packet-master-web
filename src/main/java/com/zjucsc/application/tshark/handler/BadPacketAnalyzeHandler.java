package com.zjucsc.application.tshark.handler;

import com.zjucsc.application.config.Common;
import com.zjucsc.application.config.SocketIoEvent;
import com.zjucsc.application.domain.analyzer.FiveDimensionAnalyzer;
import com.zjucsc.application.domain.analyzer.OperationAnalyzer;
import com.zjucsc.application.domain.bean.BadPacket;
import com.zjucsc.application.socketio.SocketServiceCenter;
import com.zjucsc.application.tshark.decode.AbstractAsyncHandler;
import com.zjucsc.application.tshark.domain.packet.FiveDimensionPacketWrapper;
import com.zjucsc.application.util.PacketDecodeUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

import static com.zjucsc.application.config.Common.FV_DIMENSION_FILTER;

@Slf4j
public class BadPacketAnalyzeHandler extends AbstractAsyncHandler<Void> {

    public BadPacketAnalyzeHandler(ExecutorService executor) {
        super(executor);
    }

    @Override
    public Void handle(Object t) {
        FiveDimensionPacketWrapper packet = ((FiveDimensionPacketWrapper) t);
        //System.out.println("bad packet handler : " + packet);
        operationAnalyze(packet.fiveDimensionPacket.code,packet);
        protocolAnalyze(packet);
        artifactAnalyze(packet.tcpPayload , packet);
        return null;
    }

    private void artifactAnalyze(byte[] tcpPayload, FiveDimensionPacketWrapper packet) {
        /*
         * 只有定义了工艺参数分析器的报文才需要分析，其他都不需要直接略过
         */
    }

    private ThreadLocal<AnalyzerConsumerForOperation> analyzerThreadlocalForOperation
            = ThreadLocal.withInitial(AnalyzerConsumerForOperation::new);

    private ThreadLocal<AnalyzerConsumerForFVDimension> analyzerThreadlocalForFvDimension
            = ThreadLocal.withInitial(AnalyzerConsumerForFVDimension::new);

    private void protocolAnalyze(FiveDimensionPacketWrapper packetWrapper) {
        /*
         * 调用五元组分析器进行解析【所有报文都需要进行五元组分析】
         */
        FV_DIMENSION_FILTER.forEach(analyzerThreadlocalForFvDimension.get().setPacketWrapper(packetWrapper));
    }



    private void operationAnalyze(String funCode , FiveDimensionPacketWrapper packetWrapper){
        Common.OPERATION_FILTER.forEach(analyzerThreadlocalForOperation.get().setPacketWrapper(packetWrapper,funCode));
    }

    private static class AnalyzerConsumerForOperation extends AbstractAnalyzerConsumer implements BiConsumer<Integer, ConcurrentHashMap<String, OperationAnalyzer>>{

        private FiveDimensionPacketWrapper packetWrapper;
        private String funCode = null;

        @Override
        public void accept(Integer integer, ConcurrentHashMap<String, OperationAnalyzer> stringOperationAnalyzerConcurrentHashMap) {
            OperationAnalyzer operationAnalyzer = null;
            if ((operationAnalyzer = stringOperationAnalyzerConcurrentHashMap.get(packetWrapper.fiveDimensionPacket.protocol))!=null){
                /*
                 * 只有定义了分析器的报文才需要功能码解析，其他的报文直接略过
                 */
                BadPacket badPacket = null;
                //"0x00000004" "4"   -->  fun_code of int
                int fun_code = PacketDecodeUtil.decodeFuncode(funCode
                        ,packetWrapper.fiveDimensionPacket.protocol);
                badPacket = (BadPacket) operationAnalyzer.analyze(fun_code,packetWrapper);
                if (badPacket!=null){
                    badPacket.setDeviceId(integer);
                    sendBadPacket(badPacket);
                }
            }else{
                //log.error("********************* \n not define {} 's operation analyzer , please check . \n *********************");
            }
        }

        public AnalyzerConsumerForOperation setPacketWrapper(FiveDimensionPacketWrapper packetWrapper , String fun_code){
            this.packetWrapper = packetWrapper;
            this.funCode = fun_code;
            return this;
        }
    }

    private static class AnalyzerConsumerForFVDimension extends AbstractAnalyzerConsumer implements BiConsumer<Integer, FiveDimensionAnalyzer>{

        private FiveDimensionPacketWrapper packetWrapper;
        public AnalyzerConsumerForFVDimension setPacketWrapper(FiveDimensionPacketWrapper packetWrapper){
            this.packetWrapper = packetWrapper;
            return this;
        }
        @Override
        public void accept(Integer integer, FiveDimensionAnalyzer fiveDimensionAnalyzer) {
            BadPacket badPacket = ((BadPacket)fiveDimensionAnalyzer.analyze(packetWrapper));
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
