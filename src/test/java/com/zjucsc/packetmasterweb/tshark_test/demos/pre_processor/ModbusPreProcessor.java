package com.zjucsc.packetmasterweb.tshark_test.demos.pre_processor;

import com.zjucsc.packetmasterweb.tshark_test.demos.packets.ModbusPacket;

import java.util.ArrayList;
import java.util.List;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-11 - 22:39
 */
public class ModbusPreProcessor extends BasePreProcessor<ModbusPacket> {

    @Override
    public String protocolFilterField() {
        return "modbus";
    }

    @Override
    public Class<ModbusPacket> decodeType() {
        return ModbusPacket.class;
    }

    @Override
    public List<String> filterFields() {
        return new ArrayList<String>(){
            {
                add("modbus.func_code");
            }
        };
    }
}
