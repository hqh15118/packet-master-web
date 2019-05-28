package com.zjucsc.application.tshark.domain.packet;

import com.alibaba.fastjson.annotation.JSONField;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.Arrays;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-14 - 21:16
 */
public class IEC104Packet {

    @JSONField(name = "layers")
    public LayersBean layers;

    public static class LayersBean extends FvDimensionLayer {
        @JSONField(name = "104asdu_start")
        public String[] iec60870_asdu_start;
        @JSONField(name = "104apci_apdulen")
        public String[] iec60870_104_apdulen;
        @JSONField(name = "104apci_type")
        public String[] iec60870_104_type;
        @JSONField(name = "104apci_utype")
        public String[] iec60870_104_utype;

        @Override
        public String toString() {
            return "LayersBean{" +
                    "iec60870_asdu_start=" + Arrays.toString(iec60870_asdu_start) +
                    ", iec60870_104_apdulen=" + Arrays.toString(iec60870_104_apdulen) +
                    ", iec60870_104_type=" + Arrays.toString(iec60870_104_type) +
                    ", iec60870_104_utype=" + Arrays.toString(iec60870_104_utype) +
                    ", frame_protocols=" + Arrays.toString(frame_protocols) +
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
        return "IEC104Packet{" +
                "layers=" + layers +
                '}';
    }
}
