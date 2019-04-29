package com.zjucsc.application.tshark;

import com.zjucsc.application.tshark.decode.AbstractAsyncHandler;
import com.zjucsc.application.tshark.packet.PacketInfo;
import com.zjucsc.application.tshark.packet.layers.ModbusPacket;
import com.zjucsc.application.tshark.packet.layers.S7Packet;

import java.util.concurrent.ExecutorService;

/**
 * packet json_string -> packet instance -> return five dimentsion info
 */
public class PacketDecodeHandler extends AbstractAsyncHandler<String> {

    public PacketDecodeHandler(ExecutorService executor) {
        super(executor);
    }

    @Override
    public String handle(Object t) {
        PacketInfo.PacketWrapper wrapper = (PacketInfo.PacketWrapper) t;
        switch (wrapper.packet) {
            case MODBUS:
                return ModbusPacket.decode(wrapper.json);
            case S7:
                return S7Packet.decode(wrapper.json);
            default: {
                return "unknown packet ";//FIXME
            }
        }
    }
}
