package com.zjucsc.application.tshark.domain.packet.layers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.zjucsc.application.tshark.domain.packet.FiveDimensionPacketWrapper;

public class OtherPacket{

    public LayersBean layersBean;

    public static class LayersBean {
        public String[] frame_protocols;
        public String[] eth_dst;
        public String[] eth_src;
        public String[] ip_src = {""};
        @JSONField(name ="ip_dst")
        public String[] ip_addr = {""};
        public String[] tcp_srcport = {""};
        public String[] tcp_dstport = {""};
        public String[] tcp_payload = {""};
        public String[] frame_cap_len = {""};
    }
}
