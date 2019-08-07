package com.zjucsc.application.domain.non_hessian;

import lombok.Data;

@Data
public class PacketRealTimeBean {
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
