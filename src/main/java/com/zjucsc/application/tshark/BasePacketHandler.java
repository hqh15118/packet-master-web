package com.zjucsc.application.tshark;

import com.alibaba.fastjson.JSON;
import com.zjucsc.application.tshark.decode.AbstractAsyncHandler;
import com.zjucsc.application.tshark.packet.PacketInfo;

import java.util.concurrent.ExecutorService;

public class BasePacketHandler extends AbstractAsyncHandler<PacketInfo.PacketWrapper> {

    public BasePacketHandler(ExecutorService executor) {
        super(executor);
    }

    @Override
    public PacketInfo.PacketWrapper handle(Object t) {
        return new PacketInfo.PacketWrapper(
                PacketInfo.discernPacket(JSON.parseObject((String)t,BasePacket.class).layers.frame_protocols[0]),
                (String)t);
    }
}
