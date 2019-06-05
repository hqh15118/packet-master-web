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

    private String timeStamp;
    private int protocolId;
    private String srcMac;
    private String dstMac;
    private String srcIp;
    private String dstIp;
    private String srcPort;
    private String dstPort;
    private String funcode;
    private String length;
    private int badType;
    private int danger;



}
