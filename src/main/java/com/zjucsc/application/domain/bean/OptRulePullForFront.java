package com.zjucsc.application.domain.bean;

import lombok.Data;

@Data
public class OptRulePullForFront {
    private String deviceNumber;
    private int type;
    private int cached;
    private int protocolId;
}
