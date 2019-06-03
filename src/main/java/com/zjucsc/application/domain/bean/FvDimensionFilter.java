package com.zjucsc.application.domain.bean;

import lombok.Data;


@Data
public class FvDimensionFilter {
    private int id;
    private String deviceNumber;
    private String userName;
    private int filterType;
    private String srcIp;
    private String dstIp;
    private String srcMac;
    private String dstMac;
    private String srcPort;
    private String dstPort;
    private int protocolId;
    private int gplotId;
    private String fvId;
}
