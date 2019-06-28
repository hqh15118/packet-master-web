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
    //
    private Object data;

    private String timeStamp;

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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
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

        public Builder data(Object data){
            attackBean.setData(data);
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

        public AttackBean build(){
            return attackBean;
        }
    }
}
