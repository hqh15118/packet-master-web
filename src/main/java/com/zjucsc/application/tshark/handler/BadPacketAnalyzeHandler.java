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
import com.zjucsc.application.tshark.domain.bean.BadPacket;
import com.zjucsc.application.tshark.domain.packet.UnknownPacket;
import com.zjucsc.application.util.CommonCacheUtil;
import com.zjucsc.tshark.handler.AbstractAsyncHandler;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.zjucsc.application.config.Common.*;

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
        //分析结果
        //res可能为null
        if(res!=null){
            ThreadLocalWrapper threadLocalWrapper = (ThreadLocalWrapper)res;
            //将解析出来的数据添加到LinkedList中，发送到前端
            StatisticsData.addArtMapData(threadLocalWrapper.getFloatMap());
            //攻击信息发送
            if (threadLocalWrapper.getAttackTypeList()!=null && threadLocalWrapper.getAttackTypeList().size() > 0){
                //发生了工艺参数攻击
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


    private void protocolAnalyze(FvDimensionLayer layer) {
        FiveDimensionAnalyzer fiveDimensionAnalyzer;
        //根据目的地址，定位到具体的报文过滤器进行分析
        String tag = CommonCacheUtil.getPacketFilterDstStatement(layer);
        if ((fiveDimensionAnalyzer = FV_DIMENSION_FILTER_PRO.get(tag)) != null) {
            BadPacket badPacket = ((BadPacket) fiveDimensionAnalyzer.analyze(layer));
            if (badPacket != null) {
                String deviceNumber = CommonCacheUtil.getTargetDeviceNumberByTag(tag);
                badPacket.setDeviceNumber(deviceNumber);
                //恶意报文统计
                statisticsBadPacket(badPacket, deviceNumber);
            } else {
                //五元组正常，再进行操作的匹配
                //功能码分析
                if (!(layer instanceof UnknownPacket.LayersBean)) {
                    String funCode = layer.funCode;
                    if (!"--".equals(funCode)) {
                        operationAnalyze(Integer.valueOf(funCode), layer);
                    }
                }
            }
        } else {
            BadPacket badPacket = new BadPacket.Builder(AttackTypePro.UN_KNOW_DEVICE)
                    .set_five_Dimension(layer)
                    .setDangerLevel(DangerLevel.DANGER)
                    .setComment("未知报文来源")
                    .build();
            SocketServiceCenter.updateAllClient(SocketIoEvent.BAD_PACKET, badPacket);
        }
    }

    private void operationAnalyze(int funCode , FvDimensionLayer layer) {
        //根据目的IP/MAC地址获取对应的功能码分析器
        String tag = CommonCacheUtil.getPacketFilterDstStatement(layer);
        ConcurrentHashMap<String, OperationAnalyzer> map = OPERATION_FILTER_PRO.get(tag);
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
                BadPacket badPacket;
                badPacket = (BadPacket) operationAnalyzer.analyze(funCode,layer);
                if (badPacket!=null){
                    String deviceNumber = CommonCacheUtil.getTargetDeviceNumberByTag(tag);
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

    private static Executor sendBadPacketThreadPool = Executors.newSingleThreadExecutor(
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

}
