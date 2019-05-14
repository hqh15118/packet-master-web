package com.zjucsc.application.tshark.domain.packet;


import com.alibaba.fastjson.annotation.JSONField;

import java.util.Arrays;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-11 - 19:19
 */
public class S7CommPacket{

    public static final String JOB = "1";
    public static final String ACK_DATA = "3";

    @JSONField(name = "layers")
    public LayersBean layersX;

    /**
     * timestamp : 1557129889033
     * layers : {"frame_protocols":["eth:ethertype:ip:tcp:tpkt:cotp:s7comm"],"eth_dst":["00:0c:29:49:7e:9f"],"frame_cap_len":["109"],"eth_src":["00:0c:29:75:b2:38"],"ip_src":["192.168.254.134"],"ip_dst":["192.168.254.34"],"tcp_srcport":["1073"],"tcp_dstport":["102"],"s7comm_param_func":["0x00000004"],"tcp_payload":["0300001f02f08032010000ccc1000e00000401120a10020011000184000020"],"s7comm_header_rosctr":["1"]}
     */

    public static class LayersBean extends FvDimensionLayer {
        public String[] s7comm_param_func={""};
        public String[] s7comm_header_rosctr={""};

        @Override
        public String toString() {
            return "LayersBean{" +
                    "s7comm_param_func=" + Arrays.toString(s7comm_param_func) +
                    ", s7comm_header_rosctr=" + Arrays.toString(s7comm_header_rosctr) +
                    ", frame_protocols=" + Arrays.toString(frame_protocols) +
                    ", eth_dst=" + Arrays.toString(eth_dst) +
                    ", frame_cap_len=" + Arrays.toString(frame_cap_len) +
                    ", eth_src=" + Arrays.toString(eth_src) +
                    ", ip_src=" + Arrays.toString(ip_src) +
                    ", ip_dst=" + Arrays.toString(ip_dst) +
                    ", tcp_srcport=" + Arrays.toString(tcp_srcport) +
                    ", tcp_dstport=" + Arrays.toString(tcp_dstport) +
                    ", eth_trailer=" + Arrays.toString(eth_trailer) +
                    ", eth_fcs=" + Arrays.toString(eth_fcs) +
                    ", timeStamp='" + timeStamp + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "S7CommPacket{" +
                "layersX=" + layersX +
                '}';
    }
}
