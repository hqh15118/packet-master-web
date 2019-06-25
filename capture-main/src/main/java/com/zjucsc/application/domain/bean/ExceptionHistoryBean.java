package com.zjucsc.application.domain.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class ExceptionHistoryBean implements Serializable {
    private int page;
    private int limit;
    private String start;
    private String end;
    private int badType;
    private int danger;
    private int protocolId;
    private String srcMac;
    private String dstMac;
    private String srcIp;
    private String dstIp;
    private String srcPort;
    private String dstPort;
}
