package com.zjucsc.application.tshark;

import com.corundumstudio.socketio.SocketIOClient;
import com.zjucsc.application.tshark.decode.AbstractAsyncHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

public class PacketSendHandler extends AbstractAsyncHandler<Void> {

    public static  SocketIOClient client;

    private static Logger logger = LoggerFactory.getLogger(PacketSendHandler.class);

    public PacketSendHandler(ExecutorService executor) {
        super(executor);
    }

    @Override
    public Void handle(Object t) {
        if (client != null){
            client.sendEvent("packet_event",(String)t);
            System.out.println("send msg successfully");
        }else{
           //logger.error("not define client , {} msg send failed" , t);
        }
        return null;
    }
}
