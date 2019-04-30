package com.zjucsc.application.tshark.domain.packet;

public class FiveDimensionPacket {
    public String timeStamp;
    public String protocol;
    public String src_ip;
    public String dis_ip;
    public String code;

    public FiveDimensionPacket(String timeStamp,String protocol, String src_ip, String dis_ip, String code) {
        this.timeStamp = timeStamp;
        this.protocol = protocol;
        this.src_ip = src_ip;
        this.dis_ip = dis_ip;
        this.code = code;
    }
}
