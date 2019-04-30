package com.zjucsc.application.tshark.handler;

import com.alibaba.fastjson.JSON;
import com.corundumstudio.socketio.SocketIOClient;
import com.zjucsc.application.tshark.decode.AbstractAsyncHandler;
import com.zjucsc.application.tshark.domain.packet.FiveDimensionPacketWrapper;

import java.util.concurrent.ExecutorService;

public class PacketSendHandler extends AbstractAsyncHandler<Void> {

    public static  SocketIOClient client;

    public PacketSendHandler(ExecutorService executor) {
        super(executor);
    }

    @Override
    public Void handle(Object t) {
        FiveDimensionPacketWrapper packet = ((FiveDimensionPacketWrapper) t);
        if (client != null){
            client.sendEvent("packet_event", JSON.toJSONString(packet.fiveDimensionPacket));
            System.out.println("send msg successfully");
        }else{
           //logger.error("not define client , {} msg send failed" , t);
        }
        return null;
    }

}
