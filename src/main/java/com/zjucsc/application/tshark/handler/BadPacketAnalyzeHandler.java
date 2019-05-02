package com.zjucsc.application.tshark.handler;

import com.zjucsc.application.domain.bean.BadPacket;
import com.zjucsc.application.tshark.decode.AbstractAsyncHandler;
import com.zjucsc.application.tshark.domain.beans.PacketInfo.PACKET_PROTOCOL;
import com.zjucsc.application.tshark.domain.packet.FiveDimensionPacketWrapper;
import com.zjucsc.application.util.PacketDecodeUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ExecutorService;

public class BadPacketAnalyzeHandler extends AbstractAsyncHandler<Void> {

    public BadPacketAnalyzeHandler(ExecutorService executor) {
        super(executor);
    }

    @Override
    public Void handle(Object t) {
        FiveDimensionPacketWrapper packet = ((FiveDimensionPacketWrapper) t);
        System.out.println("bad packet handler : " + packet);
        //"0x00000004" "4"   -->  fun_code of int
        int fun_code = PacketDecodeUtil.decodeFuncode(packet.fiveDimensionPacket.code
                ,packet.fiveDimensionPacket.protocol);
        switch (packet.fiveDimensionPacket.protocol){
            case PACKET_PROTOCOL.MODBUS :

                break;
            case PACKET_PROTOCOL.S7 :

                break;
            case PACKET_PROTOCOL.TCP :

                break;
            case PACKET_PROTOCOL.UDP :

                break;
            case PACKET_PROTOCOL.IP :

                break;
            case PACKET_PROTOCOL.OTHER:

                break;
            default:
                throw new RuntimeException("not define target packet protocol : " + packet.fiveDimensionPacket.protocol);
        }
        BadPacket badPacket = analyze();
        if (badPacket!=null){
            sendBadPacket(badPacket);
        }
        return null;
    }

    private void sendBadPacket(BadPacket badPacket) {

    }


    private BadPacket analyze(){
        return null;
    }
}
