package com.zjucsc.application.domain.bean;

import lombok.Data;

@Data
public class AttackConfigState {
    private int id;
    private boolean enable;
    private String protocol;
}
