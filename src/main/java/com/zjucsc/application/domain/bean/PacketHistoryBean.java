package com.zjucsc.application.domain.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class PacketHistoryBean implements Serializable {
    private int page;
    private int limit;
    private String start;
    private String end;
    private int protocolId;
    private String srcMac;
    private String dstMac;
    private String srcIp;
    private String dstIp;
    private String srcPost;
    private String dstPort;
}
