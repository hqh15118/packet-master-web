package com.zjucsc.application.tshark.handler;

import com.alibaba.fastjson.JSON;
import com.zjucsc.application.tshark.decode.AbstractAsyncHandler;
import com.zjucsc.application.tshark.domain.packet.InitPacket;
import com.zjucsc.application.tshark.domain.beans.PacketInfo;
import com.zjucsc.application.util.PacketDecodeUtil;

import java.util.concurrent.ExecutorService;

public class BasePacketHandler extends AbstractAsyncHandler<PacketInfo.PacketWrapper> {

    public BasePacketHandler(ExecutorService executor) {
        super(executor);
    }


    @Override
    public PacketInfo.PacketWrapper handle(Object t) {
        InitPacket initPacket = JSON.parseObject((String) t, InitPacket.class);
        //System.out.println("base packet handler : " + initPacket);
        return new PacketInfo.PacketWrapper(
                PacketDecodeUtil.discernPacket(initPacket),//protocol type
                (String) t,                             //json data
                initPacket.layers.tcp_payload[0]     //timestamp<decode from tcp_payload> }
        );
    }
}
