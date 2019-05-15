package com.zjucsc.application.tshark.domain.packet;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Arrays;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-14 - 20:43
 */
public class UnknownPacket {
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
                    ", tcp_srcport=" + Arrays.toString(src_port) +
                    ", tcp_dstport=" + Arrays.toString(dst_port) +
                    ", eth_trailer=" + Arrays.toString(eth_trailer) +
                    ", eth_fcs=" + Arrays.toString(eth_fcs) +
                    ", timeStamp='" + timeStamp + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "UnknownPacket{" +
                "layers=" + layers +
                '}';
    }
}
