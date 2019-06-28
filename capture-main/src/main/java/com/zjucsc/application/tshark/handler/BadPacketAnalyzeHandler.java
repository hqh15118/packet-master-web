package com.zjucsc.application.tshark.handler;

import com.zjucsc.application.config.Common;
import com.zjucsc.application.config.DangerLevel;
import com.zjucsc.application.config.PACKET_PROTOCOL;
import com.zjucsc.application.config.StatisticsData;
import com.zjucsc.application.tshark.analyzer.FiveDimensionAnalyzer;
import com.zjucsc.application.tshark.analyzer.OperationAnalyzer;
import com.zjucsc.application.tshark.domain.BadPacket;
import com.zjucsc.application.util.AppCommonUtil;
import com.zjucsc.application.util.CommonCacheUtil;
import com.zjucsc.application.util.PacketDecodeUtil;
import com.zjucsc.art_decode.ArtDecodeCommon;
import com.zjucsc.attack.bean.AttackBean;
import com.zjucsc.attack.common.AttackCommon;
import com.zjucsc.attack.common.AttackTypePro;
import com.zjucsc.socket_io.SocketIoEvent;
import com.zjucsc.socket_io.SocketServiceCenter;
import com.zjucsc.tshark.handler.AbstractAsyncHandler;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import com.zjucsc.tshark.packets.UndefinedPacket;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class BadPacketAnalyzeHandler extends AbstractAsyncHandler<Void> {

    public BadPacketAnalyzeHandler(ExecutorService executor) {
        super(executor);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Void handle(Object t) {
        FvDimensionLayer layer = ((FvDimensionLayer) t);
        //五元组分析
        protocolAnalyze(layer);

        //工艺参数分析
        byte[] tcpPayload = PacketDecodeUtil.hexStringToByteArray(layer.tcp_payload[0]);
        Map<String,Float> res = null;
        String protocol = layer.frame_protocols[0];
        if (protocol.startsWith("s7comm")){
            if (!layer.tcp_flags_ack[0].equals("")){
                res =  ArtDecodeCommon.artDecodeEntry(AppCommonUtil.getGlobalArtMap(),tcpPayload,"s7comm",1);
            }else{
                res =  ArtDecodeCommon.artDecodeEntry(AppCommonUtil.getGlobalArtMap(),layer.rawData,"s7comm",0);
            }
        }else if (protocol.equals(PACKET_PROTOCOL.MODBUS)){
            res = ArtDecodeCommon.artDecodeEntry(AppCommonUtil.getGlobalArtMap(),tcpPayload,layer.frame_protocols[0]);
        }else if (protocol.equals(PACKET_PROTOCOL.PN_IO)){
            res = ArtDecodeCommon.artDecodeEntry(AppCommonUtil.getGlobalArtMap(),layer.rawData,layer.frame_protocols[0]);
        }else if (protocol.equals(PACKET_PROTOCOL.IEC104)){
            res = ArtDecodeCommon.artDecodeEntry(AppCommonUtil.getGlobalArtMap(),tcpPayload,layer.frame_protocols[0]);
        }
        else{

        }
        //分析结果
        //res可能为null
        if(res!=null){
            //数据发送
            StatisticsData.addArtMapData(res);
            AttackCommon.appendArtAnalyze(res);
        }
        return null;
    }


    private void protocolAnalyze(FvDimensionLayer layer) {
        FiveDimensionAnalyzer fiveDimensionAnalyzer;
        //根据报文tag，定位到具体的报文过滤器进行分析
        if ((fiveDimensionAnalyzer = CommonCacheUtil.getFvDimensionFilter(layer)) != null) {
            AttackBean attackBean = ((AttackBean) fiveDimensionAnalyzer.analyze(layer));
            if (attackBean != null) {
                String deviceNumber = CommonCacheUtil.getTargetDeviceNumberByTag(layer.ip_dst[0],layer.eth_dst[0]);
                attackBean.setDeviceNumber(deviceNumber);
                //恶意报文统计
                statisticsBadPacket(deviceNumber);
                //回调对恶意报文进行发送和统计
                AttackCommon.appendFvDimensionError(attackBean);
            } else {
                //五元组正常，再进行操作的匹配
                //功能码分析
                if (!(layer instanceof UndefinedPacket.LayersBean)) {
                    String funCode = layer.funCode;
                    if (!"--".equals(funCode)) {
                        operationAnalyze(Integer.valueOf(funCode), layer);
                    }
                }
            }
        } else {
            //忽略广播包
            if (!layer.eth_dst[0].equals("ff:ff:ff:ff:ff:ff")&&!layer.ip_dst[0].equals("255:255:255:255")) {
                AttackBean attackBean = new AttackBean.Builder()
                        .attackInfo("未定义该报文对应的设备")
                        .attackType(AttackTypePro.UN_KNOW_DEVICE)
                        .fvDimension(layer)
                        .build();
                //回调对恶意报文进行发送和统计
                AttackCommon.appendFvDimensionError(attackBean);
            }
        }
    }

    private void operationAnalyze(int funCode , FvDimensionLayer layer) {
        //根据目的IP/MAC地址获取对应的功能码分析器
        ConcurrentHashMap<String, OperationAnalyzer> map = CommonCacheUtil.getOptAnalyzer(layer);
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
            if ((operationAnalyzer = map.get(layer.frame_protocols[0]))!=null){
                AttackBean attackBean;
                attackBean = (AttackBean) operationAnalyzer.analyze(funCode,layer);
                if (attackBean!=null){
                    String deviceNumber = CommonCacheUtil.getTargetDeviceNumberByTag(layer.ip_dst[0],layer.eth_dst[0]);
                    attackBean.setDeviceNumber(deviceNumber);
                    statisticsBadPacket(deviceNumber);
                }
            }else{
                log.debug("can not find protocol {} 's operation analyzer" , layer.frame_protocols[0]);
            }
        }
    }

    private void statisticsBadPacket(String deviceNumber){
        StatisticsData.attackNumber.incrementAndGet();
        StatisticsData.increaseAttackByDevice(deviceNumber);
    }

}
