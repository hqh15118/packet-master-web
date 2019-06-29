package com.zjucsc.attack.bean;

import com.zjucsc.tshark.packets.FvDimensionLayer;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-30 - 20:06
 */

public class AttackBean {

    private AttackBean(){}
    //攻击类型
    private String attackType;
    //攻击说明
    private String attackInfo;

    private String timeStamp;

    private String srcMac;
    private String dstMac;
    private String srcIp;
    private String dstIp;
    private String srcPort;
    private String dstPort;
    private String protocolName;
    private String funCode;
    private String deviceNumber;

    public String getAttackInfo() {
        return attackInfo;
    }

    public String getSrcMac() {
        return srcMac;
    }

    public void setSrcMac(String srcMac) {
        this.srcMac = srcMac;
    }

    public String getDstMac() {
        return dstMac;
    }

    public void setDstMac(String dstMac) {
        this.dstMac = dstMac;
    }

    public String getSrcIp() {
        return srcIp;
    }

    public void setSrcIp(String srcIp) {
        this.srcIp = srcIp;
    }

    public String getDstIp() {
        return dstIp;
    }

    public void setDstIp(String dstIp) {
        this.dstIp = dstIp;
    }

    public String getSrcPort() {
        return srcPort;
    }

    public void setSrcPort(String srcPort) {
        this.srcPort = srcPort;
    }

    public String getDstPort() {
        return dstPort;
    }

    public void setDstPort(String dstPort) {
        this.dstPort = dstPort;
    }

    public String getProtocolName() {
        return protocolName;
    }

    public void setProtocolName(String protocolName) {
        this.protocolName = protocolName;
    }

    public String getFunCode() {
        return funCode;
    }

    public void setFunCode(String funCode) {
        this.funCode = funCode;
    }

    public void setAttackInfo(String attackInfo) {
        this.attackInfo = attackInfo;
    }

    public String getAttackType() {
        return attackType;
    }

    public void setAttackType(String attackBean) {
        this.attackType = attackBean;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public static Builder builder(){
        return new Builder();
    }

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public static class Builder{

        private AttackBean attackBean;

        public Builder(){
            attackBean = new AttackBean();
        }

        public Builder attackType(String attackType){
            attackBean.setAttackType(attackType);
            return this;
        }

        public Builder attackInfo(String attackInfo){
            attackBean.setAttackInfo(attackInfo);
            return this;
        }

        public Builder timeStamp(String timeStamp){
            attackBean.timeStamp = timeStamp;
            return this;
        }

        public Builder fvDimension(FvDimensionLayer layer){
            attackBean.setFunCode(layer.funCode);
            attackBean.setSrcIp(layer.ip_src[0]);
            attackBean.setDstIp(layer.ip_dst[0]);
            attackBean.setSrcPort(layer.src_port[0]);
            attackBean.setDstPort(layer.dst_port[0]);
            attackBean.setSrcMac(layer.eth_src[0]);
            attackBean.setDstMac(layer.eth_dst[0]);
            attackBean.setProtocolName(layer.frame_protocols[0]);
            return this;
        }

        public AttackBean build(){
            return attackBean;
        }
    }
}
