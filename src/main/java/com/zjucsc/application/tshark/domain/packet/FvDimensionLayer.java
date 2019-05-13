package com.zjucsc.application.tshark.domain.packet;

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
    public String[] tcp_srcport = {""};
    public String[] tcp_dstport = {""};
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
