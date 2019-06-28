package com.zjucsc.application.domain.bean;

import lombok.Data;

import java.util.List;

@Data
public class ArtAttackConfig {

    private String protocolId;
    private List<ArtAttack2Config> rule;
    private String detail;
    private boolean enable;
}
