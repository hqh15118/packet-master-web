package com.zjucsc.application.domain.bean;

import lombok.Data;

@Data
public class Protocol {
    private int protocolId;
    private String opt;

    public Protocol(int protocolId, String opt) {
        this.protocolId = protocolId;
        this.opt = opt;
    }
}
