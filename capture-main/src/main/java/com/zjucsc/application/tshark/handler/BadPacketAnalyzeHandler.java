package com.zjucsc.application.tshark.handler;

import com.zjucsc.application.config.Common;
import com.zjucsc.application.config.PACKET_PROTOCOL;
import com.zjucsc.application.domain.bean.RightPacketInfo;
import com.zjucsc.application.tshark.analyzer.FiveDimensionAnalyzer;
import com.zjucsc.application.tshark.analyzer.OperationAnalyzer;
import com.zjucsc.application.util.CacheUtil;
import com.zjucsc.application.util.PacketDecodeUtil;
import com.zjucsc.attack.bean.AttackBean;
import com.zjucsc.attack.AttackCommon;
import com.zjucsc.attack.common.AttackTypePro;
import com.zjucsc.tshark.handler.AbstractAsyncHandler;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import com.zjucsc.tshark.packets.UndefinedPacket;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

@Slf4j
public class BadPacketAnalyzeHandler extends AbstractAsyncHandler<Void> {

    private ThreadLocal<RightPacketInfo> rightPacketInfoThreadLocal =
            ThreadLocal.withInitial(RightPacketInfo::new);

    public BadPacketAnalyzeHandler(ExecutorService executor) {
        super(executor);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Void handle(Object t) {
        FvDimensionLayer layer = ((FvDimensionLayer) t);
        //五元组分析
        protocolAnalyze(layer);
        return null;
    }


    private void protocolAnalyze(FvDimensionLayer layer) {
        FiveDimensionAnalyzer fiveDimensionAnalyzer;
        //根据报文tag，定位到具体的报文过滤器进行分析
        RightPacketInfo rightPacketInfo = rightPacketInfoThreadLocal.get();
        rightPacketInfo.setDst_ip(layer.ip_dst[0]);
        rightPacketInfo.setSrc_ip(layer.ip_src[0]);
        rightPacketInfo.setDst_mac(layer.eth_dst[0]);
        rightPacketInfo.setSrc_mac(layer.eth_src[0]);
        rightPacketInfo.setFunCode(layer.funCode);
        rightPacketInfo.setProtocol(layer.protocol);
        //判断是否是白名单报文
        if (CacheUtil.isNormalRightPacket(rightPacketInfo)){
            operationAnalyze(layer);
        }else {
            if ((fiveDimensionAnalyzer = CacheUtil.getFvDimensionFilter(layer)) != null) {
                //sniff
                //eth:llc:data
                String protocolStack = layer.frame_protocols[0];
                if (protocolStack.length() >= 8 && protocolStack.charAt(4) == 'l' && protocolStack.charAt(5) == 'l'
                        && protocolStack.charAt(6) == 'c' && layer.rawData[14]==(byte)0xaa && layer.rawData[15]==(byte)0xaa
                && layer.rawData[20]==(byte) 0x01 && layer.rawData[21]==(byte)0xfd){
                    AttackCommon.appendFvDimensionError(AttackBean.builder().attackType(AttackTypePro.SNIFF_ATTACK)
                            .fvDimension(layer).build(),layer);
                }
                AttackBean attackBean = ((AttackBean) fiveDimensionAnalyzer.analyze(layer));
                if (attackBean != null) {
                    //statisticsBadPacket(deviceNumber);
                    //回调对恶意报文进行发送和统计【实时推送】
                    AttackCommon.appendFvDimensionError(attackBean,layer);
                } else {
                    //五元组正常，再进行操作的匹配
                    //功能码分析
                    operationAnalyze(layer);
                }
            }
        }

    }

    private void operationAnalyze(FvDimensionLayer layer){
        if (!(layer instanceof UndefinedPacket.LayersBean)) {
            String funCode = layer.funCode;
            //有功能码
            if (!"--".equals(funCode)) {
                doOperationAnalyze(layer.funCode, layer);
            }
        }
    }

    private void doOperationAnalyze(String funCode , FvDimensionLayer layer) {
        //根据目的IP/MAC地址获取对应的功能码分析器
        ConcurrentHashMap<String, OperationAnalyzer> map = CacheUtil.getOptAnalyzer(layer);
        String protocol = checkProtocol(layer);
        if (map != null){
            OperationAnalyzer operationAnalyzer;
            /**
             * 只有定义了分析器的报文[即配置了过滤规则的]才需要功能码解析，其他的报文直接略过
             * layer.frame_protocols[0] 这个协议已经被统一过了
             * 统一成：
             * @see PACKET_PROTOCOL
             * 缓存在：
             * @see Common 的 【PROTOCOL_STR_TO_INT】
             */
            if ((operationAnalyzer = map.get(protocol))!=null){
                Object attackBean = operationAnalyzer.analyze(funCode,layer,protocol);
                if (attackBean!=null){
                    AttackCommon.appendFvDimensionError(((AttackBean) attackBean),layer);
                    //statisticsBadPacket(deviceNumber);
                }
            }else{
                if (!layer.funCode.equals("--")){
                    AttackCommon.appendFvDimensionError(AttackBean.builder().attackType(AttackTypePro.VISIT_COMMAND)
                    .fvDimension(layer).attackInfo(PacketDecodeUtil.getAttackBeanInfo(layer.protocol,layer.funCode))
                    .build(),layer);
                }
            }
        }
    }



    private String checkProtocol(FvDimensionLayer layer){
        if (layer.protocol.equals("s7comm")){
            if (Common.systemRunType !=0 ) {
                return PacketDecodeUtil.decodeS7Protocol(layer);
            }else{
                return PacketDecodeUtil.decodeS7Protocol2(layer);
            }
        }else {
            return PacketDecodeUtil.discernPacket(layer);
        }
    }
}
