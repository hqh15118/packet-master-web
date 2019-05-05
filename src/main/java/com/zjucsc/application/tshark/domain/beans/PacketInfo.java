package com.zjucsc.application.tshark.domain.beans;

import com.zjucsc.application.tshark.domain.packet.FiveDimensionPacketWrapper;

public class PacketInfo {
    /**
     * base handler --> xxxhandler
     */
    public static class PacketWrapper{
        public String packetProtocol;
        public String json;
        public String tcpPayload;
        public String packetLength;
        public PacketWrapper(String packetProtocol,String json,String tcpPayload){
            this.packetProtocol = packetProtocol;
            this.json = json;
            this.tcpPayload = tcpPayload;
        }

        public PacketWrapper(){

        }

        @Override
        public String toString() {
            return "PacketWrapper{" +
                    "packetProtocol='" + packetProtocol + '\'' +
                    ", json='" + json + '\'' +
                    ", tcpPayload='" + tcpPayload + '\'' +
                    ", packetLength='" + packetLength + '\'' +
                    '}';
        }

        public FiveDimensionPacketWrapper fiveDimensionPacketWrapper;

        public PacketWrapper onFiveDimensionPacketWrapper(FiveDimensionPacketWrapper fiveDimensionPacketWrapper){
            this.fiveDimensionPacketWrapper = fiveDimensionPacketWrapper;
            return this;
        }
    }

}
