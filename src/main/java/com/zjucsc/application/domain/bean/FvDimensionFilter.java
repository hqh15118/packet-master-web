package com.zjucsc.application.domain.bean;


import lombok.Data;

@Data
public class FvDimensionFilter {
    private String deviceNumber;
    private String srcIp;
    private String dstIp;
    private String srcMac;
    private String dstMac;
    private String srcPort;
    private String dstPort;
    private int protocolId;
    private String fvId;
    private String opts;
}
