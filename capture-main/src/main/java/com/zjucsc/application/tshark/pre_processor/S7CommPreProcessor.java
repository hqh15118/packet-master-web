package com.zjucsc.application.tshark.pre_processor;

import com.zjucsc.tshark.TsharkCommon;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import com.zjucsc.tshark.packets.S7CommPacket;
import com.zjucsc.tshark.pre_processor.SinglePreProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-11 - 20:06
 */
public class S7CommPreProcessor extends SinglePreProcessor<S7CommPacket> {

    @Override
    public String singleProtocolFilterField() {
        return "s7comm";
    }

    @Override
    public Class<S7CommPacket> decodeType() {
        return S7CommPacket.class;
    }

    @Override
    public List<String> filterFields() {
        return new ArrayList<String>(){
            {
                add("s7comm.param.func");
                add("s7comm.header.rosctr");
                add("s7comm.param.userdata.funcgroup");
            }
        };
    }

    @Override
    public FvDimensionLayer decode(S7CommPacket packetInstance) {
        return packetInstance.layersX;
    }

    @Override
    public String filter() {
        return TsharkCommon.s7comm_filter == null ? super.filter() : TsharkCommon.s7comm_filter;
    }
}
