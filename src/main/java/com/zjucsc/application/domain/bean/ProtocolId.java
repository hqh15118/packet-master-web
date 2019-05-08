package com.zjucsc.application.domain.bean;

import lombok.Data;

@Data
public class ProtocolId {
    private int protocolId;
    private String opt;

    public ProtocolId(int protocolId, String opt) {
        this.protocolId = protocolId;
        this.opt = opt;
    }
}
