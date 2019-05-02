package com.zjucsc.application.tshark.domain.packet;


public class FiveDimensionPacketWrapper {
    public FiveDimensionPacket fiveDimensionPacket;
    public String tcpPayload;

    public FiveDimensionPacketWrapper(String timeStamp,String protocol, String src_ip, String dis_ip, String code,
                                      String tcpPayload) {
        fiveDimensionPacket = new FiveDimensionPacket(  timeStamp,
                                                        protocol,
                                                        src_ip,
                                                        dis_ip,
                                                        code);
        this.tcpPayload = tcpPayload;
    }

    public FiveDimensionPacketWrapper(){

    }

    public static class Builder{
        private FiveDimensionPacketWrapper wrapper;
        public Builder(){
            wrapper = new FiveDimensionPacketWrapper();
            wrapper.fiveDimensionPacket = new FiveDimensionPacket();
        }

        public Builder timeStamp(String timeStamp){
            wrapper.fiveDimensionPacket.timeStamp = timeStamp;
            return this;
        }

        public Builder protocol(String protocol){
            wrapper.fiveDimensionPacket.protocol = protocol;
            return this;
        }

        public Builder src_Ip(String src_Ip){
            wrapper.fiveDimensionPacket.src_ip = src_Ip;
            return this;
        }

        public Builder dst_Ip(String dst_Ip){
            wrapper.fiveDimensionPacket.dis_ip = dst_Ip;
            return this;
        }

        public Builder fun_code(String fun_code){
            wrapper.fiveDimensionPacket.code = fun_code;
            return this;
        }

        public Builder tcpPayload(String tcpPayload){
            wrapper.tcpPayload = tcpPayload;
            return this;
        }

        public FiveDimensionPacketWrapper build(){
            return wrapper;
        }
    }

    @Override
    public String toString() {
        return "FiveDimensionPacketWrapper{" +
                "fiveDimensionPacket=" + fiveDimensionPacket +
                ", tcpPayload='" + tcpPayload + '\'' +
                '}';
    }
}
