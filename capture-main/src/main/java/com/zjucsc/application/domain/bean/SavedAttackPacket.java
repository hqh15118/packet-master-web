package com.zjucsc.application.domain.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class SavedAttackPacket implements Serializable {
    /**
     * timeStamp :  1
     * protocolName : 1
     * srcMac : 1
     * dstMac : 1
     * srcIp : 1
     * dstIp : 1
     * srcPort :  1
     * dstPort : 1
     * funcode : 1
     * length : 1
     */

    private String packetTimeStamp;
    private String protocolName;
    private String srcMac;
    private String dstMac;
    private String srcIp;
    private String dstIp;
    private String srcPort;
    private String dstPort;
    private String funCode;
    private String length;
    private int danger;
    private String attackInfo;
    private String attackType;
    private String protocol;
}
