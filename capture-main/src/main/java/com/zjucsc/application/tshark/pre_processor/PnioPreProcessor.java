package com.zjucsc.application.tshark.pre_processor;

import com.zjucsc.application.config.PACKET_PROTOCOL;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import com.zjucsc.tshark.packets.PnioPacket;
import com.zjucsc.tshark.pre_processor.SinglePreProcessor;

import java.util.ArrayList;
import java.util.List;

public class PnioPreProcessor extends SinglePreProcessor<PnioPacket> {
    @Override
    public FvDimensionLayer decode(PnioPacket packetInstance) {
        return packetInstance.getLayers();
    }

    @Override
    public Class<PnioPacket> decodeType() {
        return PnioPacket.class;
    }

    @Override
    public String singleProtocolFilterField() {
        return "pn_io";
    }

    @Override
    public List<String> filterFields() {
        return new ArrayList<>();
    }
}
