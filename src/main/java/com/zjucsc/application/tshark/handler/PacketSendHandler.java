package com.zjucsc.application.tshark.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.zjucsc.application.config.SocketIoEvent;
import com.zjucsc.application.socketio.SocketServiceCenter;
import com.zjucsc.application.tshark.decode.AbstractAsyncHandler;
import com.zjucsc.application.tshark.domain.packet.FiveDimensionPacketWrapper;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.ExecutorService;

@Slf4j
public class PacketSendHandler extends AbstractAsyncHandler<Void> {

    public PacketSendHandler(ExecutorService executor) {
        super(executor);
    }

    @Override
    public Void handle(Object t) {
        FiveDimensionPacketWrapper packet = ((FiveDimensionPacketWrapper) t);
        SocketServiceCenter.updateAllClient(SocketIoEvent.ALL_PACKET,packet.fiveDimensionPacket);
        return null;
    }

}
