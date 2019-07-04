package com.zjucsc.application.tshark.pre_processor;

import com.zjucsc.tshark.packets.Dnp3_0Packet;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import com.zjucsc.tshark.pre_processor.SinglePreProcessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-14 - 21:30
 */
public class DNP3_0PreProcessor extends SinglePreProcessor<Dnp3_0Packet> {
    @Override
    public FvDimensionLayer decode(Dnp3_0Packet packetInstance) {
        return packetInstance.layers;
    }

    @Override
    public String singleProtocolFilterField() {
        return "dnp3";
    }

    @Override
    public Class<Dnp3_0Packet> decodeType() {
        return Dnp3_0Packet.class;
    }

    @Override
    public List<String> filterFields() {
        return new ArrayList<String>(){
            {
                add("dnp3.ctl.prm");
                add("dnp3.ctl.prifunc");
                add("dnp3.ctl.secfunc");
            }
        };
    }
}
