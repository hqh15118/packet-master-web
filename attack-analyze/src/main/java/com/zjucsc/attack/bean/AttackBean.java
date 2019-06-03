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
    //
    private AttackType attackType;
    //异常的五元组
    private FvDimensionLayer layer;
    //攻击说明
    private String attackInfo;
    //
    private Object data;

    public FvDimensionLayer getLayer() {
        return layer;
    }

    public void setLayer(FvDimensionLayer layer) {
        this.layer = layer;
    }

    public String getAttackInfo() {
        return attackInfo;
    }

    public void setAttackInfo(String attackInfo) {
        this.attackInfo = attackInfo;
    }

    public AttackType getAttackType() {
        return attackType;
    }

    public void setAttackType(AttackType attackBean) {
        this.attackType = attackBean;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{

        private AttackBean attackBean;

        public Builder(){
            attackBean = new AttackBean();
        }

        public Builder attackType(AttackType attackType){
            attackBean.setAttackType(attackType);
            return this;
        }

        public Builder data(Object data){
            attackBean.setData(data);
            return this;
        }

        public Builder fvDimensionLayer(FvDimensionLayer layer){
            attackBean.setLayer(layer);
            return this;
        }

        public Builder attackInfo(String attackInfo){
            attackBean.setAttackInfo(attackInfo);
            return this;
        }

        public AttackBean build(){
            return attackBean;
        }
    }
}
