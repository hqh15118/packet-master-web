package com.zjucsc.application.domain.bean;

import lombok.Data;

@Data
public class AttackType {
    /**
     * 攻击类型
     */
    private String type;
    /**
     * 该攻击类型的次数
     */
    private int value;
}
