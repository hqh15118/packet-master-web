package com.zjucsc.application.domain.bean;

import lombok.Data;

@Data
public class OptConfigEnable {
    private int protocolId;
    private boolean enable;
    private String opName;
}
