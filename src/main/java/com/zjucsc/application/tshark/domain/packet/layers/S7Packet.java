package com.zjucsc.application.tshark.domain.packet.layers;

import com.alibaba.fastjson.annotation.JSONField;

public class S7Packet{


    /**
     * timestamp : 946695632244
     * layers : {"frame_protocols":["eth:ethertype:ip:tcp:tpkt:cotp:s7comm"],"eth_dst":["00:0c:29:49:7e:9f"],"eth_src":["00:0c:29:75:b2:38"],"ip_src":["192.168.254.134"],"ip_addr":["192.168.254.134","192.168.254.34"],"tcp_srcport":["1073"],"tcp_dstport":["102"],"s7comm_param_func":["0x00000004"],"tcp_payload":["03:00:00:1f:02:f0:80:32:01:00:00:cc:c1:00:0e:00:00:04:01:12:0a:10:02:00:11:00:01:84:00:00:20"]}
     */

    public LayersBean layers;

    public static class LayersBean {
        //public String[] frame_protocols;
        public String[] eth_dst;
        public String[] eth_src;
        public String[] ip_src;
        @JSONField(name ="ip_dst")
        public String[] ip_addr;
        public String[] tcp_srcport;
        public String[] tcp_dstport;
        public String[] s7comm_param_func;
        //public String[] tcp_payload;
        //public String[] s7comm_header_rosctr;
    }

    public static final String JOB = "1",ACK_DATA = "3";
}
