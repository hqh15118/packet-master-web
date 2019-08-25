package com.zjucsc.attack.bean;

import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.Objects;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-30 - 20:06
 */

public class AttackBean implements Comparable<AttackBean>{

    private AttackBean(){}
    //攻击类型
    private String attackType;
    //攻击说明
    private String attackInfo;
    //下面的这些都在五元组里面设置了
    private String srcMac;
    private String dstMac;
    private String srcIp;
    private String dstIp;
    private String srcPort;
    private String dstPort;
    private String protocolName;
    private String funCode;
    private String deviceNumber;
    private String packetTimeStamp;
    private int length;
    private String srcDevice;
    private String dstDevice;
    private String funCodeMeaning;
    private String rawData;

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

    public String getSrcDevice() {
        return srcDevice;
    }

    public void setSrcDevice(String srcDevice) {
        this.srcDevice = srcDevice;
    }

    public String getDstDevice() {
        return dstDevice;
    }

    public void setDstDevice(String dstDevice) {
        this.dstDevice = dstDevice;
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

    public String getFunCodeMeaning() {
        return funCodeMeaning;
    }

    public void setFunCodeMeaning(String funCodeMeaning) {
        this.funCodeMeaning = funCodeMeaning;
    }

    public static Builder builder(){
        return new Builder();
    }

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public String getPacketTimeStamp() {
        return packetTimeStamp;
    }

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    public void setPacketTimeStamp(String packetTimeStamp) {
        this.packetTimeStamp = packetTimeStamp;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    @Override
    public int compareTo(AttackBean o) {
        if (equals(o)) {
            return 0;
        }else{
            if (o.hashCode() > hashCode()){
                return 1;
            }else{
                return -1;
            }
        }
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

        public Builder fvDimension(FvDimensionLayer layer){
            attackBean.setFunCode(layer.funCode);
            attackBean.setSrcIp(layer.ip_src[0]);
            attackBean.setDstIp(layer.ip_dst[0]);
            attackBean.setSrcPort(layer.src_port[0]);
            attackBean.setDstPort(layer.dst_port[0]);
            attackBean.setSrcMac(layer.eth_src[0]);
            attackBean.setDstMac(layer.eth_dst[0]);
            attackBean.setProtocolName(layer.protocol);
            attackBean.setPacketTimeStamp(layer.timeStamp);
            attackBean.setLength(layer.rawData.length);
            attackBean.setRawData(layer.custom_ext_raw_data[0]);
            attackBean.setFunCodeMeaning(layer.funCodeMeaning);
            attackBean.setDeviceNumber(layer.deviceNumber);
            return this;
        }

        public AttackBean build(){
            return attackBean;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AttackBean)) return false;
        AttackBean that = (AttackBean) o;
        return  getAttackType().equals(that.getAttackType()) &&
                getAttackInfo().equals(that.getAttackInfo()) &&
                getProtocolName().equals(that.getProtocolName()) &&
                getFunCode().equals(that.getFunCode()) &&
                getPacketTimeStamp().equals(that.getPacketTimeStamp()) &&
                getSrcDevice().equals(that.getSrcDevice()) &&
                getDstDevice().equals(that.getDstDevice()) &&
                getFunCodeMeaning().equals(that.getFunCodeMeaning());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAttackType(), getAttackInfo(), getProtocolName(), getFunCode(), getDeviceNumber(), getPacketTimeStamp(), getSrcDevice(), getDstDevice(), getFunCodeMeaning());
    }
}
