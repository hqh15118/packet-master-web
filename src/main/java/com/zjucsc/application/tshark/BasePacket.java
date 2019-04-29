package com.zjucsc.application.tshark;

import java.util.List;

public class BasePacket {


    /**
     * timestamp : 946695632201
     * layers : {"frame_protocols":["eth:ethertype:ip:tcp:mbtcp:modbus"],"eth_dst":["00:0c:29:9e:7a:44"],"eth_src":["00:0c:29:75:b2:38"],"ip_src":["192.168.254.134"],"ip_addr":["192.168.254.134","192.168.254.143"],"tcp_srcport":["1075"],"tcp_dstport":["502"],"modbus_func_code":["2"],"tcp_payload":["d9:04:00:00:00:06:01:02:00:00:00:0b"]}
     */

    public String timestamp;
    public LayersBean layers;

    public static class LayersBean {
        public String[] frame_protocols;
    }


}
