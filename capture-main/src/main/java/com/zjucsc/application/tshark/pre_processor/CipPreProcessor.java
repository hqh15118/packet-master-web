package com.zjucsc.application.tshark.pre_processor;

import com.zjucsc.tshark.packets.CipPacket;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import com.zjucsc.tshark.pre_processor.SinglePreProcessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CipPreProcessor extends SinglePreProcessor<CipPacket> {
    @Override
    public FvDimensionLayer decode(CipPacket packetInstance) {
        return packetInstance.layers;
    }

    @Override
    public Class<CipPacket> decodeType() {
        return CipPacket.class;
    }

    @Override
    public String singleProtocolFilterField() {
        return "cip";
    }

    @Override
    public List<String> filterFields() {
        return new ArrayList<String>(){
            {
                add("cip.sc");
            }
        };
    }
}
