package com.zjucsc.packetmasterweb.tshark_test.demos.pre_processor;

import com.zjucsc.application.tshark.domain.packets_2.S7CommPacket;

import java.util.ArrayList;
import java.util.List;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-11 - 20:06
 */
public class S7CommPreProcessor extends BasePreProcessor<S7CommPacket> {

    @Override
    public String protocolFilterField() {
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
            }
        };
    }
}
