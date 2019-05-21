package com.zjucsc.application.tshark.domain.packet;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Arrays;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-11 - 21:23
 */

public class FvDimensionLayer {
    public String[] frame_protocols = {""};
    public String[] eth_dst = {""};
    public String[] frame_cap_len = {""};
    public String[] eth_src = {""};
    public String[] ip_src = {""};
    public String[] ip_dst = {""};
    @JSONField(name = "tcp_srcport")
    public String[] src_port = {""};
    @JSONField(name = "tcp_dstport")
    public String[] dst_port = {""};
    public String[] eth_trailer = {""};
    public String[] eth_fcs = {""};
    public String timeStamp = "";
    public String[] tcp_payload = {""};

    public FvDimensionLayer setFrameProtocols(String protocol){
        if (protocol!=null) {
            frame_protocols[0] = protocol;
        }
        return this;
    }

    @Override
    public String toString() {
        return "FvDimensionLayer{" +
                "frame_protocols=" + Arrays.toString(frame_protocols) +
                ", eth_dst=" + Arrays.toString(eth_dst) +
                ", frame_cap_len=" + Arrays.toString(frame_cap_len) +
                ", eth_src=" + Arrays.toString(eth_src) +
                ", ip_src=" + Arrays.toString(ip_src) +
                ", ip_dst=" + Arrays.toString(ip_dst) +
                ", src_port=" + Arrays.toString(src_port) +
                ", dst_port=" + Arrays.toString(dst_port) +
                ", eth_trailer=" + Arrays.toString(eth_trailer) +
                ", eth_fcs=" + Arrays.toString(eth_fcs) +
                ", timeStamp='" + timeStamp + '\'' +
                ", tcp_payload=" + Arrays.toString(tcp_payload) +
                '}';
    }
}
