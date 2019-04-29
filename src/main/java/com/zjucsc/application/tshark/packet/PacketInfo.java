package com.zjucsc.application.tshark.packet;

public class PacketInfo {
    public enum _PACKET{
        MODBUS,
        S7,
        OTHER
    }
    public static _PACKET discernPacket(String protocolStack){
        switch (protocolStack.substring(protocolStack.length() - 3)){
            case "bus": return _PACKET.MODBUS;
            case "omm" : return _PACKET.S7;
            default:return _PACKET.OTHER;
        }
    }

    public static class PacketWrapper{
        public _PACKET packet;
        public String json;
        public PacketWrapper(_PACKET packet,String json){
            this.packet = packet;
            this.json = json;
        }
    }
}
