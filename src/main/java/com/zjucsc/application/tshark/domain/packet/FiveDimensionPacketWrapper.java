package com.zjucsc.application.tshark.domain.packet;


public class FiveDimensionPacketWrapper {
    public FiveDimensionPacket fiveDimensionPacket;
    public String rawData;

    public FiveDimensionPacketWrapper(String timeStamp,String protocol, String src_ip, String dis_ip, String code,
                                      String rawData) {
        fiveDimensionPacket = new FiveDimensionPacket(  timeStamp,
                                                        protocol,
                                                        src_ip,
                                                        dis_ip,
                                                        code);
        this.rawData = rawData;
    }
}
