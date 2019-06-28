package com.zjucsc.attack.bean;

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

    public String getAttackInfo() {
        return attackInfo;
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

    public static class Builder{

        private AttackBean attackBean;

        public Builder(){
            attackBean = new AttackBean();
        }

        public Builder attackType(String attackType){
            attackBean.setAttackType(attackType);
            return this;
        }

        public Builder srcMac(String srcMac){
            attackBean.srcMac = srcMac;
            return this;
        }

        public Builder dstMac(String dstMac){
            attackBean.dstMac = dstMac;
            return this;
        }

        public Builder srcIp(String srcIp){
            attackBean.srcIp = srcIp;
            return this;
        }

        public Builder dstIp(String dstIp){
            attackBean.dstIp = dstIp;
            return this;
        }

        public Builder srcPort(String srcPort){
            attackBean.srcPort = srcPort;
            return this;
        }

        public Builder dstPort (String dstPort){
            attackBean.dstPort = dstPort;
            return this;
        }

        public Builder attackInfo(String attackInfo){
            attackBean.setAttackInfo(attackInfo);
            return this;
        }

        public Builder funCode(String funCode){
            attackBean.funCode = funCode;
            return this;
        }

        public Builder timeStamp(String timeStamp){
            attackBean.timeStamp = timeStamp;
            return this;
        }

        public AttackBean build(){
            return attackBean;
        }
    }
}
