package com.zjucsc.application.tshark.domain.packet;

public class FiveDimensionPacket {
    public String timeStamp;
    public String protocol;
    public String src_ip;
    public String dis_ip;
    public String code;
    public String dis_port;
    public String src_port;

    public FiveDimensionPacket() {
    }

    public FiveDimensionPacket(String timeStamp,String protocol, String src_ip,
                               String dis_ip, String dis_port,String src_port,
                               String code) {
        this.timeStamp = timeStamp;
        this.protocol = protocol;
        this.src_ip = src_ip;
        this.dis_ip = dis_ip;
        this.dis_port = dis_port;
        this.src_port = src_port;
        this.code = code;
    }

    @Override
    public String toString() {
        return "FiveDimensionPacket{" +
                "timeStamp='" + timeStamp + '\'' +
                ", protocol='" + protocol + '\'' +
                ", src_ip='" + src_ip + '\'' +
                ", dis_ip='" + dis_ip + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
