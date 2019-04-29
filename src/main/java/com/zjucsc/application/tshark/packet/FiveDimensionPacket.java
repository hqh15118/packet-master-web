package com.zjucsc.application.tshark.packet;


public class FiveDimensionPacket {
    public String src_ip;
    public String dis_ip;
    public String src_port;
    public String dis_port;
    public String code;
    public String rawData;

    public FiveDimensionPacket(String src_ip, String dis_ip, String src_port, String dis_port, String code,
                               String rawData) {
        this.src_ip = src_ip;
        this.dis_ip = dis_ip;
        this.src_port = src_port;
        this.dis_port = dis_port;
        this.code = code;
        this.rawData = rawData;
    }
}
