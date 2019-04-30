package com.zjucsc.application.tshark.handler;

import com.alibaba.fastjson.JSON;
import com.zjucsc.application.tshark.decode.AbstractAsyncHandler;
import com.zjucsc.application.tshark.domain.packet.InitPacket;
import com.zjucsc.application.tshark.domain.beans.PacketInfo;

import java.util.concurrent.ExecutorService;

public class BasePacketHandler extends AbstractAsyncHandler<PacketInfo.PacketWrapper> {

    public BasePacketHandler(ExecutorService executor) {
        super(executor);
    }

    @Override
    public PacketInfo.PacketWrapper handle(Object t) {
        InitPacket initPacket = JSON.parseObject((String)t, InitPacket.class);
        return new PacketInfo.PacketWrapper(
                PacketInfo.discernPacket(initPacket.layers.frame_protocols[0]),
                (String)t,                             //json data
                initPacket.layers.tcp_payload[1],     //timestamp<decode from tcp_payload>
                initPacket.layers.frame_cap_len[2]);  //packet_length
    }

}
