package com.zjucsc.application.tshark.domain.packet;

public class InitPacket {


    /**
     * timestamp : 946739510664
     * layers : {"frame_protocols":["eth:ethertype:ip:tcp:tpkt:cotp:s7comm"],"tcp_payload":["03:00:00:1f:02:f0:80:32:01:00:00:cc:c1:00:0e:00:00:04:01:12:0a:10:02:00:11:00:01:84:00:00:20"],"frame_cap_len":["85"]}
     */

    //private String timestamp;
    public LayersBean layers;


    public static class LayersBean {
        public String[] frame_protocols;
        public String[] tcp_payload;
        public String[] frame_cap_len;
    }
}
