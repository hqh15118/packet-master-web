package com.zjucsc.application.tshark.domain.beans;

public class PacketInfo {
    /**
     * base handler --> xxxhandler
     */
    public static class PacketWrapper{
        public String packetProtocol;
        public String json;
        public String tcpPayload;
        public String packetLength;
        public PacketWrapper(String packetProtocol,String json,String tcpPayload,String packetLength){
            this.packetProtocol = packetProtocol;
            this.json = json;
            this.tcpPayload = tcpPayload;
            this.packetLength = packetLength;
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
    }

}
