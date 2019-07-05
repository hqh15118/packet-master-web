package com.zjucsc.tshark.packets;

import com.alibaba.fastjson.annotation.JSONField;
import com.oracle.webservices.internal.api.databinding.DatabindingMode;

import java.io.Serializable;
import java.util.Arrays;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-11 - 21:23
 */

public class FvDimensionLayer {
    //用于前端显示的协议
    public String protocol;
    //更细致的协议
    public String[] frame_protocols = {"--"};
    public String[] eth_dst = {"--"};
    public String[] frame_cap_len = {"--"};
    public String[] eth_src = {"--"};
    public String[] ip_src = {"--"};
    public String[] ip_dst = {"--"};
    @JSONField(name = "tcp_srcport")
    public String[] src_port = {"--"};
    @JSONField(name = "tcp_dstport")
    public String[] dst_port = {"--"};
    //public String[] eth_trailer = {""};
    //public String[] eth_fcs = {""};
    public String timeStamp = "";
    public String[] tcp_payload = {""};
    @JSONField(deserialize = false)
    public String funCode = "--";
    @JSONField(deserialize = false)
    public String funCodeMeaning = "--";
    //long格式的时间戳 , ns 为单位
    @JSONField(deserialize = false,serialize = false)
    public long timeStampInLong;
    public String[] tcp_flags_ack={""};
    public String[] tcp_flags_syn={""};
    //raw data
    public String[] custom_ext_raw_data = {""};
    @JSONField(deserialize = false)
    public int delay;
    @JSONField(deserialize = false)
    public int collectorId;
    @JSONField(serialize = false,deserialize = false)
    public byte[] rawData;

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
                ", timeStamp='" + timeStamp + '\'' +
                ", tcp_payload=" + Arrays.toString(tcp_payload) +
                ", funCode=" + funCode +
                ", funCodeMeaning='" + funCodeMeaning + '\'' +
                '}';
    }

}
