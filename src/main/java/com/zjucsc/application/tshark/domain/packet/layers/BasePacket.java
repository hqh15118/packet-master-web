package com.zjucsc.application.tshark.domain.packet.layers;

public abstract class BasePacket {
    private String protocolName;
    BasePacket(){
        protocolName = protocolName();
    }
    public abstract String protocolName();
    public String getProtocolName(){
        return protocolName;
    }
}
