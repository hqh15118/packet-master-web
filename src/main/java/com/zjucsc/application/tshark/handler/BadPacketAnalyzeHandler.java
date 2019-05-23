package com.zjucsc.application.tshark.handler;

import com.zjucsc.application.config.AttackTypePro;
import com.zjucsc.application.config.DangerLevel;
import com.zjucsc.application.config.SocketIoEvent;
import com.zjucsc.application.config.StatisticsData;
import com.zjucsc.application.domain.bean.ThreadLocalWrapper;
import com.zjucsc.application.domain.exceptions.ProtocolIdNotValidException;
import com.zjucsc.application.socketio.SocketServiceCenter;
import com.zjucsc.application.tshark.analyzer.FiveDimensionAnalyzer;
import com.zjucsc.application.tshark.analyzer.OperationAnalyzer;
import com.zjucsc.application.tshark.decode.AbstractAsyncHandler;
import com.zjucsc.application.tshark.domain.bean.BadPacket;
import com.zjucsc.application.tshark.domain.packet.FvDimensionLayer;
import com.zjucsc.application.tshark.domain.packet.ModbusPacket;
import com.zjucsc.application.tshark.domain.packet.S7CommPacket;
import com.zjucsc.application.tshark.domain.packet.UnknownPacket;
import com.zjucsc.application.util.CommonCacheUtil;
import com.zjucsc.application.util.PacketDecodeUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.zjucsc.application.config.Common.*;
import static com.zjucsc.application.config.PACKET_PROTOCOL.MODBUS;
import static com.zjucsc.application.config.PACKET_PROTOCOL.S7;

@Slf4j
public class BadPacketAnalyzeHandler extends AbstractAsyncHandler<Void> {

    public BadPacketAnalyzeHandler(ExecutorService executor) {
        super(executor);
    }


    @SuppressWarnings("unchecked")
    @Override
    public Void handle(Object t) {
        FvDimensionLayer layer = ((FvDimensionLayer) t);
        //五元组分析，如果正常，则回调进行操作码分析，如果操作码正常，则回调进行工艺参数分析
        protocolAnalyze(layer);

        //工艺参数分析
        Object res = ART_FILTER.analyze(layer.tcp_payload[0] , layer.frame_protocols[0]);
        if(res!=null){
            ThreadLocalWrapper threadLocalWrapper = (ThreadLocalWrapper)res;
            StatisticsData.addArtMapData(threadLocalWrapper.getFloatMap());
            if (threadLocalWrapper.getAttackTypeList()!=null && threadLocalWrapper.getAttackTypeList().size() > 0){
                SocketServiceCenter.updateAllClient(SocketIoEvent.ATTACK_INFO,
                        new BadPacket.Builder(AttackTypePro.HAZARD_ART)
                            .setDangerLevel(DangerLevel.VERY_DANGER)
                            .set_five_Dimension(layer)
                            .build());
                threadLocalWrapper.getAttackTypeList().clear();
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

    private void protocolAnalyze(FvDimensionLayer layer) {
        /*
         * 调用五元组分析器进行解析【所有报文都需要进行五元组分析】
         */
        //FV_DIMENSION_FILTER_PRO.forEach(analyzerThreadlocalForFvDimension.get().setPacketWrapper(layer));
        FiveDimensionAnalyzer fiveDimensionAnalyzer = null;
        //根据目的地址，定位到具体的报文过滤器进行分析
        if((fiveDimensionAnalyzer = FV_DIMENSION_FILTER_PRO.get(layer.ip_dst[0])) != null){
            BadPacket badPacket = ((BadPacket)fiveDimensionAnalyzer.analyze(layer));
            if (badPacket!=null){
                String deviceNumber = CommonCacheUtil.getTargetDeviceNumberByIp(layer.ip_dst[0]);
                badPacket.setDeviceNumber(deviceNumber);
                //恶意报文统计
                statisticsBadPacket(badPacket , deviceNumber);
            }else{
                //五元组正常，再进行操作的匹配
                //功能码分析
                if (!(layer instanceof UnknownPacket.LayersBean)) {
                    int funCode = decodeFuncode(layer);
                    if (funCode!=0) {
                        try {
                            operationAnalyze(funCode, layer);
                        } catch (ProtocolIdNotValidException e) {
                            log.error("protocol Id not valid {} " , e.getMsg());
                        }
                    }
                }
            }
        }else{
            log.debug("not define ip : {}  fv_dimension_filter" , layer.ip_dst[0]);
        }
    }

    /*
        private static class AnalyzerConsumerForFVDimension extends AbstractAnalyzerConsumer implements BiConsumer<String, FiveDimensionAnalyzer>{

            private FvDimensionLayer fvDimensionLayer;
            private OKCallback fvPacketOKCallback;

            public AnalyzerConsumerForFVDimension setPacketWrapper(FvDimensionLayer fvDimensionLayer){
                this.fvDimensionLayer = fvDimensionLayer;
                return this;
            }
            @Override
            public void accept(String deviceNumber, FiveDimensionAnalyzer fiveDimensionAnalyzer) {
                BadPacket badPacket = ((BadPacket)fiveDimensionAnalyzer.analyze(fvDimensionLayer));
                if (badPacket!=null){
                    badPacket.setDeviceId(deviceNumber);
                    //恶意报文统计
                    statisticsBadPacket(badPacket , deviceNumber);
                }else{
                    //五元组正常，再进行操作的匹配
                    fvPacketOKCallback.callback(fvDimensionLayer);
                }
            }

            public void setFvOKCallback(OKCallback fvOKCallback){
                this.fvPacketOKCallback = fvOKCallback;
            }
        }
        */

    private void operationAnalyze(int funCode , FvDimensionLayer layer) throws ProtocolIdNotValidException {
        //OPERATION_FILTER_PRO.forEach(analyzerThreadlocalForOperation.get().setPacketWrapper(layer,funCode));
        //根据目的IP地址获取对应的功能码分析器
        ConcurrentHashMap<String, OperationAnalyzer> map = OPERATION_FILTER_PRO.get(layer.ip_dst[0]);
        if (map != null){
            OperationAnalyzer operationAnalyzer = null;
            /**
             * 只有定义了分析器的报文[即配置了过滤规则的]才需要功能码解析，其他的报文直接略过
             * layer.frame_protocols[0] 这个协议已经被统一过了
             * 统一成：
             * @see com.zjucsc.application.config.PACKET_PROTOCOL
             * 缓存在：
             * @see com.zjucsc.application.config.Common 的 【PROTOCOL_STR_TO_INT】
             */
            if ((operationAnalyzer = map.get(layer.frame_protocols[0]))!=null){
                BadPacket badPacket = null;
                try {
                    badPacket = (BadPacket) operationAnalyzer.analyze(funCode,layer);
                } catch (ProtocolIdNotValidException e) {
                    log.error(" " , e);
                }
                if (badPacket!=null){
                    String deviceNumber = CommonCacheUtil.getTargetDeviceNumberByIp(layer.ip_dst[0]);
                    badPacket.setDeviceNumber(deviceNumber);
                    statisticsBadPacket(badPacket , deviceNumber);
                }else{
//                    Object res = ART_FILTER.analyze(layer.tcp_payload[0] , layer.frame_protocols[0]);
//                    System.out.println(res);
//                    if (res!=null){
//                        SocketServiceCenter.updateAllClient(SocketIoEvent.GRAPH_INFO,res);
//                    }
                }
            }else{
                log.debug("can not find protocol {} 's operation analyzer" , layer.frame_protocols[0]);
            }
        }
    }

    public static Executor sendBadPacketThreadPool = Executors.newSingleThreadExecutor(
            r -> {
                Thread thread = new Thread(r);
                thread.setName("-BadPacketAnalyzeHandler-");
                return thread;
            }
    );

    private void sendBadPacket(BadPacket badPacket) {
        sendBadPacketThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                //System.out.println("send bad packet : " + badPacket);
                //System.out.println("**********************");
                //TODO KAFKA 发送到数据库
                SocketServiceCenter.updateAllClient(SocketIoEvent.BAD_PACKET,badPacket);
            }
        });
    }

    private void statisticsBadPacket(BadPacket badPacket, String deviceNumber){
        sendBadPacket(badPacket);       //发送恶意报文
        if (badPacket.getDangerLevel() == DangerLevel.VERY_DANGER){     //统计恶意报文
            StatisticsData.attackNumber.incrementAndGet();
            StatisticsData.increaseAttackByDevice(deviceNumber);
        }else {                                                         //统计异常报文
            StatisticsData.exceptionNumber.incrementAndGet();           //总数+1
            StatisticsData.increaseExceptionByDevice(deviceNumber);     //按设备+1
        }
    }

    //    private static class AnalyzerConsumerForOperation extends AbstractAnalyzerConsumer implements BiConsumer<String, ConcurrentHashMap<String, OperationAnalyzer>>{
//
//        private FvDimensionLayer layer;
//        private int funCode;
//        private OKCallback optOkCallback;
//
//        @Override                                               //String是协议，OperationAnalyzer是对应的报文规则器
//        public void accept(String deviceNumber, ConcurrentHashMap<String, OperationAnalyzer> stringOperationAnalyzerConcurrentHashMap) {
//            OperationAnalyzer operationAnalyzer = null;
//            /**
//             * 只有定义了分析器的报文[即配置了过滤规则的]才需要功能码解析，其他的报文直接略过
//             * layer.frame_protocols[0] 这个协议已经被统一过了
//             */
//            if ((operationAnalyzer = stringOperationAnalyzerConcurrentHashMap.get(layer.frame_protocols[0]))!=null){
//                BadPacket badPacket = null;
//                try {
//                    badPacket = (BadPacket) operationAnalyzer.analyze(funCode,layer);
//                } catch (ProtocolIdNotValidException e) {
//                    log.error(" " , e);
//                }
//                if (badPacket!=null){
//                    badPacket.setDeviceNumber(deviceNumber);
//                    statisticsBadPacket(badPacket , deviceNumber);
//                }else{
//                    optOkCallback.callback(layer);
//                }
//            }else{
//                //log.error("********************* \n not define {} 's operation analyzer , please check . \n *********************");
//            }
//        }
//
//        public AnalyzerConsumerForOperation setPacketWrapper(FvDimensionLayer layer , int fun_code){
//            this.layer = layer;
//            this.funCode = fun_code;
//            return this;
//        }
//
//        public void setOkCallback(OKCallback optOkCallback){
//            this.optOkCallback = optOkCallback;
//        }
//    }
//
//    private static class AbstractAnalyzerConsumer {
//
//        public static Executor sendBadPacketThreadPool = Executors.newSingleThreadExecutor(
//                r -> {
//                    Thread thread = new Thread(r);
//                    thread.setName("-BadPacketAnalyzeHandler-");
//                    return thread;
//                }
//        );
//
//        private void sendBadPacket(BadPacket badPacket) {
//            sendBadPacketThreadPool.execute(new Runnable() {
//                @Override
//                public void run() {
//                    //System.out.println("send bad packet : " + badPacket);
//                    //System.out.println("**********************");
//                    SocketServiceCenter.updateAllClient(SocketIoEvent.BAD_PACKET,badPacket);
//                }
//            });
//        }
//
//        void statisticsBadPacket(BadPacket badPacket,String deviceNumber){
//            sendBadPacket(badPacket);       //发送恶意报文
//            if (badPacket.getDangerLevel() == DangerLevel.VERY_DANGER){     //统计恶意报文
//                StatisticsData.attackNumber.incrementAndGet();
//                StatisticsData.increaseAttackByDevice(deviceNumber);
//            }else {
//                StatisticsData.exceptionNumber.incrementAndGet();
//                StatisticsData.increaseExceptionByDevice(deviceNumber);
//            }
//        }
//    }
//
//    public interface OKCallback{
//        void callback(FvDimensionLayer layer);
//    }


}
