package com.zjucsc.attack.bean;

import com.zjucsc.tshark.packets.FvDimensionLayer;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-30 - 20:06
 */

public class AttackBean {
    //异常的五元组
    private FvDimensionLayer layer;
    //攻击说明
    private String attackInfo;

    public AttackBean(FvDimensionLayer layer, String attackInfo) {
        this.layer = layer;
        this.attackInfo = attackInfo;
    }

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
}
