package com.zjucsc.capture_main_distribute.tshark.pre_processor;

import com.zjucsc.common.common_config.PACKET_PROTOCOL;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import com.zjucsc.tshark.packets.TcpPacket;
import com.zjucsc.tshark.pre_processor.SinglePreProcessor;

import java.util.ArrayList;
import java.util.List;

public class TcpPreProcessor extends SinglePreProcessor<TcpPacket> {
    @Override
    public FvDimensionLayer decode(TcpPacket packetInstance) {
        return packetInstance.layers.setFrameProtocols(PACKET_PROTOCOL.TCP);
    }

    @Override
    public Class<TcpPacket> decodeType() {
        return TcpPacket.class;
    }

    @Override
    public String singleProtocolFilterField() {
        return "tcp";
    }

    @Override
    public List<String> filterFields() {
        return new ArrayList<>();
    }
}
