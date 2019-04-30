package com.zjucsc.application.tshark.domain.packet.layers;

import com.alibaba.fastjson.JSON;
import com.zjucsc.application.tshark.domain.packet.FiveDimensionPacketWrapper;

public class OtherPacket extends BasePacket{

    public static FiveDimensionPacketWrapper decode(String protocolName , String json){
        OtherPacket otherPacket = JSON.parseObject(json, OtherPacket.class);
        OtherPacket.LayersBean layers = otherPacket.layersBean;
        return new FiveDimensionPacketWrapper(
                null,
                protocolName,
                layers.ip_src[0],
                layers.ip_addr[0],
                "",
                ""
        );
    }

    public LayersBean layersBean;

    @Override
    public String protocolName() {
        return "";
    }

    public static class LayersBean {
        public String[] frame_protocols;
        //public String[] eth_dst;
        //public String[] eth_src;
        public String[] ip_src = {""};
        public String[] ip_addr = {""};
        public String[] tcp_srcport = {""};
        public String[] tcp_dstport = {""};
    }
}
