package com.zjucsc.application.domain.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class PacketInfoExport implements Serializable {
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
