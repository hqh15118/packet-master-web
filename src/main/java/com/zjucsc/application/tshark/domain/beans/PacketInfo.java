package com.zjucsc.application.tshark.domain.beans;

public class PacketInfo {
    //TODO add protocol here
    public interface PACKET_PROTOCOL{
        String MODBUS = "modbus";
        String S7 = "s7comm";
        String TCP = "tcp";
        String IP = "ip";
        String UDP = "udp";
        String OTHER = "";
    }

    /**
     * 用于识别报文协议
     * @param protocolStack entherner:ip:tcp:...
     * @return packet protocol
     */
    public static String discernPacket(String protocolStack){
        switch (protocolStack.substring(protocolStack.length() - 3)){
            case "bus": return PACKET_PROTOCOL.MODBUS;
            case "omm" : return PACKET_PROTOCOL.S7;
            default:return PACKET_PROTOCOL.OTHER;
        }
    }

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
