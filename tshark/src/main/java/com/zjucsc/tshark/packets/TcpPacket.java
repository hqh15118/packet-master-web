package com.zjucsc.tshark.packets;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Arrays;

public class TcpPacket {

    @JSONField(name = "layers")
    public LayersBean layers;

    public static class LayersBean extends FvDimensionLayer {
        @Override
        public String toString() {
            return "LayersBean{" +
                    "frame_protocols=" + Arrays.toString(frame_protocols) +
                    ", eth_dst=" + Arrays.toString(eth_dst) +
                    ", frame_cap_len=" + Arrays.toString(frame_cap_len) +
                    ", eth_src=" + Arrays.toString(eth_src) +
                    ", ip_src=" + Arrays.toString(ip_src) +
                    ", ip_dst=" + Arrays.toString(ip_dst) +
                    ", src_port=" + Arrays.toString(src_port) +
                    ", dst_port=" + Arrays.toString(dst_port) +
                    ", timeStamp='" + timeStamp + '\'' +
                    ", tcp_payload=" + Arrays.toString(tcp_payload) +
                    ", funCode='" + funCode + '\'' +
                    ", funCodeMeaning='" + funCodeMeaning + '\'' +
                    ", timeStampInLong=" + timeStampInLong +
                    ", tcp_flags_ack=" + Arrays.toString(tcp_flags_ack) +
                    ", tcp_flags_syn=" + Arrays.toString(tcp_flags_syn) +
                    '}';
        }
    }
}
