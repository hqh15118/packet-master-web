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

public class FvDimensionLayer implements Serializable{
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

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String[] getFrame_protocols() {
        return frame_protocols;
    }

    public void setFrame_protocols(String[] frame_protocols) {
        this.frame_protocols = frame_protocols;
    }

    public String[] getEth_dst() {
        return eth_dst;
    }

    public void setEth_dst(String[] eth_dst) {
        this.eth_dst = eth_dst;
    }

    public String[] getFrame_cap_len() {
        return frame_cap_len;
    }

    public void setFrame_cap_len(String[] frame_cap_len) {
        this.frame_cap_len = frame_cap_len;
    }

    public String[] getEth_src() {
        return eth_src;
    }

    public void setEth_src(String[] eth_src) {
        this.eth_src = eth_src;
    }

    public String[] getIp_src() {
        return ip_src;
    }

    public void setIp_src(String[] ip_src) {
        this.ip_src = ip_src;
    }

    public String[] getIp_dst() {
        return ip_dst;
    }

    public void setIp_dst(String[] ip_dst) {
        this.ip_dst = ip_dst;
    }

    public String[] getSrc_port() {
        return src_port;
    }

    public void setSrc_port(String[] src_port) {
        this.src_port = src_port;
    }

    public String[] getDst_port() {
        return dst_port;
    }

    public void setDst_port(String[] dst_port) {
        this.dst_port = dst_port;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String[] getTcp_payload() {
        return tcp_payload;
    }

    public void setTcp_payload(String[] tcp_payload) {
        this.tcp_payload = tcp_payload;
    }

    public String getFunCode() {
        return funCode;
    }

    public void setFunCode(String funCode) {
        this.funCode = funCode;
    }

    public String getFunCodeMeaning() {
        return funCodeMeaning;
    }

    public void setFunCodeMeaning(String funCodeMeaning) {
        this.funCodeMeaning = funCodeMeaning;
    }

    public long getTimeStampInLong() {
        return timeStampInLong;
    }

    public void setTimeStampInLong(long timeStampInLong) {
        this.timeStampInLong = timeStampInLong;
    }

    public String[] getTcp_flags_ack() {
        return tcp_flags_ack;
    }

    public void setTcp_flags_ack(String[] tcp_flags_ack) {
        this.tcp_flags_ack = tcp_flags_ack;
    }

    public String[] getTcp_flags_syn() {
        return tcp_flags_syn;
    }

    public void setTcp_flags_syn(String[] tcp_flags_syn) {
        this.tcp_flags_syn = tcp_flags_syn;
    }

    public String[] getCustom_ext_raw_data() {
        return custom_ext_raw_data;
    }

    public void setCustom_ext_raw_data(String[] custom_ext_raw_data) {
        this.custom_ext_raw_data = custom_ext_raw_data;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getCollectorId() {
        return collectorId;
    }

    public void setCollectorId(int collectorId) {
        this.collectorId = collectorId;
    }

    public byte[] getRawData() {
        return rawData;
    }

    public void setRawData(byte[] rawData) {
        this.rawData = rawData;
    }
}
