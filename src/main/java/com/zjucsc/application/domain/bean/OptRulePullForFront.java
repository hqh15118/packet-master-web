package com.zjucsc.application.domain.bean;

import lombok.Data;

@Data
public class OptRulePullForFront {
    private int deviceId;
    private int type;
    private int cached;
    private int protocolId;
}
