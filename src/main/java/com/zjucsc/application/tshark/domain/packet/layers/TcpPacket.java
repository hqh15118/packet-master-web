package com.zjucsc.application.tshark.domain.packet.layers;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-03 - 23:16
 */
public class TcpPacket {
    public TcpPacket.LayersBean layers;

    public static class LayersBean {
        public String[] frame_protocols;
        public String[] eth_dst;
        public String[] eth_src;
        public String[] ip_src;
        @JSONField(name ="ip_dst")
        public String[] ip_addr;
        public String[] tcp_srcport;
        public String[] tcp_dstport;
        public String[] frame_cap_len = {""};
    }
}
