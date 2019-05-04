package com.zjucsc.application.domain.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-02 - 15:31
 */
@Data
/**
 * 攻击类型
 */
public class AttackType {
    private int attackType;
    private BadPacket attackObj;

    /**
     *
     * @param attackType 攻击种类 [恶意操作、未定义的报文、恶意工艺参数]
     * @param attackObj 具体的攻击信息
     */
    public AttackType(int attackType, BadPacket attackObj) {
        this.attackType = attackType;
        this.attackObj = attackObj;
    }
}
