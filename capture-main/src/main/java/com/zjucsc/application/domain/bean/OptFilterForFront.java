package com.zjucsc.application.domain.bean;


import lombok.Data;

import java.util.List;

@Data
public class OptFilterForFront {
    private int protocolId;
    private String fvId;
    private List<String> funCodes;
    private String deviceNumber;
    private boolean enable;
    private String srcMac;
    private String srcIp;

    @Override
    public String toString() {
        return "OptFilterForFront{" +
                "protocolId=" + protocolId +
                ", fvId='" + fvId + '\'' +
                ", funCodes=" + funCodes +
                ", deviceNumber='" + deviceNumber + '\'' +
                '}';
    }
}
