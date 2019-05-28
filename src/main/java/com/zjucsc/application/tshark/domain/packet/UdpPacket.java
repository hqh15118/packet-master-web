package com.zjucsc.application.tshark.domain.packet;

import com.alibaba.fastjson.annotation.JSONField;
import com.zjucsc.tshark.packets.FvDimensionLayer;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-14 - 21:16
 */
public class UdpPacket {

    @JSONField(name = "layers")
    public LayersBean layers;

    public static class LayersBean extends FvDimensionLayer {
        public String data;
        public String udp_srcport;
        public String udp_dstport;
    }
}
