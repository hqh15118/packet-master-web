package com.zjucsc.application.tshark.packet.layers;

import com.alibaba.fastjson.JSON;
import com.zjucsc.application.tshark.packet.FiveDimensionPacket;

import java.util.List;

public class S7Packet {


    /**
     * timestamp : 946695632244
     * layers : {"frame_protocols":["eth:ethertype:ip:tcp:tpkt:cotp:s7comm"],"eth_dst":["00:0c:29:49:7e:9f"],"eth_src":["00:0c:29:75:b2:38"],"ip_src":["192.168.254.134"],"ip_addr":["192.168.254.134","192.168.254.34"],"tcp_srcport":["1073"],"tcp_dstport":["102"],"s7comm_param_func":["0x00000004"],"tcp_payload":["03:00:00:1f:02:f0:80:32:01:00:00:cc:c1:00:0e:00:00:04:01:12:0a:10:02:00:11:00:01:84:00:00:20"]}
     */

    public String timestamp;
    public LayersBean layers;

    public static String decode(String s7json){
        S7Packet s7Packet = JSON.parseObject(s7json, S7Packet.class);
        LayersBean layers = s7Packet.layers;
        return JSON.toJSONString(new FiveDimensionPacket(
                layers.ip_src[0],
                layers.ip_addr[0],
                layers.tcp_srcport[0],
                layers.tcp_dstport[0],
                layers.s7comm_param_func[0],
                layers.tcp_payload[0]
        ));
    }


    public static class LayersBean {
        //public String[] frame_protocols;
        //public String[] eth_dst;
        //public String[] eth_src;
        public String[] ip_src;
        public String[] ip_addr;
        public String[] tcp_srcport;
        public String[] tcp_dstport;
        public String[] s7comm_param_func;
        public String[] tcp_payload;
    }
}
