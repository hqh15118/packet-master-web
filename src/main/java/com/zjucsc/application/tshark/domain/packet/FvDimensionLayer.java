package com.zjucsc.application.tshark.domain.packet;

import com.alibaba.fastjson.annotation.JSONField;

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

    public FvDimensionLayer setFrameProtocols(String protocol){
        if (protocol!=null) {
            frame_protocols[0] = protocol;
        }
        return this;
    }

}
