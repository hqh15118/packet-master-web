package com.zjucsc.application.domain.bean;

import com.zjucsc.application.util.CommonCacheUtil;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import lombok.Data;

@Data
public class ArtPacketDetail {
//    private String timeStamp;
//    private String srcIp;
//    private String dstIp;
//    private String srcMac;
//    private String dstMac;
//    private String srcDevice;
//    private String dstDevice;
//    private String protocol;
//    private String funCode;
//    private float value;
    private FvDimensionLayer layer;
    private String artName;
    private float value;
    public static ArtPacketDetail newOne(FvDimensionLayer layer){
        ArtPacketDetail artPacketDetail = new ArtPacketDetail();
//        artPacketDetail.setDstDevice(CommonCacheUtil.convertDeviceNumberToName(CommonCacheUtil.getTargetDeviceNumberByTag(layer.ip_dst[0],layer.eth_dst[0])));
//        artPacketDetail.setSrcDevice(CommonCacheUtil.convertDeviceNumberToName(CommonCacheUtil.getTargetDeviceNumberByTag(layer.ip_src[0],layer.eth_src[0])));
//        artPacketDetail.setSrcIp(layer.ip_src[0]);
//        artPacketDetail.setDstDevice(layer.ip_dst[0]);
//        artPacketDetail.setSrcMac(layer.eth_src[0]);
//        artPacketDetail.setDstMac(layer.eth_dst[0]);
//        artPacketDetail.setProtocol(layer.protocol);
//        artPacketDetail.setFunCode(layer.funCodeMeaning);
//        artPacketDetail.setTimeStamp(layer.timeStamp);
        artPacketDetail.setLayer(layer);
        return artPacketDetail;
    }
}
