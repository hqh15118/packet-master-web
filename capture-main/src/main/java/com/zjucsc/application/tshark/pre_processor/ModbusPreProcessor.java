package com.zjucsc.application.tshark.pre_processor;

import com.zjucsc.tshark.packets.FvDimensionLayer;
import com.zjucsc.tshark.packets.ModbusPacket;
import com.zjucsc.tshark.pre_processor.SinglePreProcessor;

import java.util.ArrayList;
import java.util.List;

<<<<<<< HEAD
import static com.zjucsc.tshark.util.PacketDecodeUtil.discernPacket;

=======
import static com.zjucsc.application.util.PacketDecodeUtil.discernPacket;
import static com.zjucsc.tshark.TsharkCommon.modbus_filter;
>>>>>>> 65e3cc7b906a3e5bad979c77c974dd267cf3c989

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-11 - 22:39
 */
public class ModbusPreProcessor extends SinglePreProcessor<ModbusPacket> {

    @Override
    public String singleProtocolFilterField() {
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

    /**
     * 将协议栈中的协议信息替换为本地定义的协议作统一当做唯一协议
     * @param packetInstance
     * @return
     */
    @Override
    public FvDimensionLayer decode(ModbusPacket packetInstance) {
        return packetInstance.layers;
    }

    @Override
    public String filter() {
        return modbus_filter == null ? super.filter() : modbus_filter;
    }
}
