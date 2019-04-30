package com.zjucsc.application.tshark.handler;

import com.zjucsc.application.tshark.decode.AbstractAsyncHandler;
import com.zjucsc.application.tshark.domain.beans.PacketInfo;
import com.zjucsc.application.tshark.domain.packet.FiveDimensionPacketWrapper;
import com.zjucsc.application.tshark.domain.packet.layers.ModbusPacket;
import com.zjucsc.application.tshark.domain.packet.layers.OtherPacket;
import com.zjucsc.application.tshark.domain.packet.layers.S7Packet;

import java.util.concurrent.ExecutorService;

import static com.zjucsc.application.tshark.domain.beans.PacketInfo.PACKET_PROTOCOL.MODBUS;
import static com.zjucsc.application.tshark.domain.beans.PacketInfo.PACKET_PROTOCOL.S7;

/**
 * packet json_string -> packet instance -> return five dimentsion info
 */
public class PacketDecodeHandler extends AbstractAsyncHandler<FiveDimensionPacketWrapper> {

    public PacketDecodeHandler(ExecutorService executor) {
        super(executor);
    }

    @Override
    public FiveDimensionPacketWrapper handle(Object t) {
        PacketInfo.PacketWrapper wrapper = (PacketInfo.PacketWrapper) t;
        switch (wrapper.packetProtocol) {
            case MODBUS:
                return ModbusPacket.decode(wrapper.packetProtocol,wrapper.json);
            case S7:
                return S7Packet.decode(wrapper.packetProtocol,wrapper.json);
            default: {
                return OtherPacket.decode(wrapper.packetProtocol,wrapper.json);
            }
        }
    }

}
