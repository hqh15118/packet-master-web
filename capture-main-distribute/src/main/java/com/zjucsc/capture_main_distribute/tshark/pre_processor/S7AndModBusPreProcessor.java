package com.zjucsc.capture_main_distribute.tshark.pre_processor;

import com.zjucsc.tshark.packets.FvDimensionLayer;
import com.zjucsc.tshark.pre_processor.MultiPreProcessor;

import java.util.List;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-17 - 23:33
 */
public class S7AndModBusPreProcessor extends MultiPreProcessor {
    @Override
    public String[] packetProtocolStacks() {
        return new String[0];
    }

    @Override
    public FvDimensionLayer outPacketIndexInStacks(int index, String packetJSON) {
        return null;
    }

    @Override
    public String[] protocolFilterField() {
        return null;
    }

    @Override
    public List<String> filterFields() {
        return null;
    }
}
