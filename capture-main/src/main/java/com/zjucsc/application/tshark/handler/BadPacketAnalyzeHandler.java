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
import sun.misc.Cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

@Slf4j
public class BadPacketAnalyzeHandler extends AbstractAsyncHandler<FvDimensionLayer> {

    private ThreadLocal<RightPacketInfo> rightPacketInfoThreadLocal =
            ThreadLocal.withInitial(RightPacketInfo::new);

    public BadPacketAnalyzeHandler(ExecutorService executor) {
        super(executor);
    }

    @SuppressWarnings("unchecked")
    @Override
    public FvDimensionLayer handle(Object t) {
        FvDimensionLayer layer = ((FvDimensionLayer) t);
        //五元组分析
        String deviceNumber = layer.deviceNumber;
        if (deviceNumber!=null) {
            //如果在白名单协议里面，直接全部跳过
            if (CacheUtil.isNormalWhiteProtocol(deviceNumber,layer.protocol))//判断是否在白名单协议之内
            {
                return layer;
            }
        }
        protocolAnalyze(layer);
        return layer;
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
            //sniff
            //eth:llc:data
            String protocolStack = layer.frame_protocols[0];
            //判断是否是嗅探攻击
            if (protocolStack.length() >= 8 && protocolStack.charAt(4) == 'l' && protocolStack.charAt(5) == 'l'
                    && protocolStack.charAt(6) == 'c' && layer.rawData[14]==(byte)0xaa && layer.rawData[15]==(byte)0xaa
                    && layer.rawData[20]==(byte) 0x01 && layer.rawData[21]==(byte)0xfd
                    && layer.eth_dst[0].equals("ff:ff:ff:ff:ff:ff")){
                AttackCommon.appendFvDimensionError(AttackBean.builder().attackType(AttackTypePro.SNIFF_ATTACK)
                        .fvDimension(layer).build(),layer);
                return;
            }

            Map<String,FiveDimensionAnalyzer> analyzerMap = CacheUtil.getSrcAnalyzerMap(layer);
            if (analyzerMap != null){
                if ((fiveDimensionAnalyzer = CacheUtil.getAnalyzerBySrcTag(analyzerMap,layer)) != null) {
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
                }else{
                    /**
                     * 非授权访问设备
                     */
                    AttackCommon.appendFvDimensionError(AttackBean.builder()
                            .fvDimension(layer)
                            .attackType(AttackTypePro.VISIT_DEVICE)
                            .attackInfo(!layer.ip_src[0].equals("--") ? layer.ip_src[0] : layer.eth_src[0])
                            .build(),layer);
                }
            }

        }

    }

    private void operationAnalyze(FvDimensionLayer layer){
        String funCode = layer.funCode;
        //有功能码
        if (!"--".equals(funCode)) {
            doOperationAnalyze(layer.funCode, layer);
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
                Object attackBean = operationAnalyzer.analyze(funCode,layer);
                if (attackBean!=null){
                    AttackCommon.appendFvDimensionError(((AttackBean) attackBean),layer);
                    //statisticsBadPacket(deviceNumber);
                }
            }else{
                if (!layer.funCode.equals("--")){
                    AttackCommon.appendFvDimensionError(AttackBean.builder().attackType(AttackTypePro.VISIT_COMMAND)
                    .fvDimension(layer).attackInfo(PacketDecodeUtil.getAttackBeanInfo(layer))
                    .build(),layer);
                }
            }
        }
    }



    private String checkProtocol(FvDimensionLayer layer){
        switch (layer.protocol){
            case "s7comm" :
                if (Common.systemRunType !=0 ) {
                    return PacketDecodeUtil.decodeS7Protocol(layer);
                }else{
                    return PacketDecodeUtil.decodeS7Protocol2(layer);
                }
            case "dnp3":
                return PacketDecodeUtil.getPacketDetailProtocol(layer);
            case "104apci":
                return PacketDecodeUtil.getIEC104DetailType(layer);
            default:return PacketDecodeUtil.discernPacket(layer.frame_protocols[0]);
        }

    }
}
