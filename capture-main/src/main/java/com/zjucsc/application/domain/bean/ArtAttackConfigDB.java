package com.zjucsc.application.domain.bean;

import lombok.Data;

@Data
public class ArtAttackConfigDB {
    private int id;
    private int protocolId;
    private String detail;
    private String ruleString;
    private String ruleJson;
    private boolean enable;
}
