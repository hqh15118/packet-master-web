package com.zjucsc.application.tshark.pre_processor;

import com.zjucsc.application.config.PACKET_PROTOCOL;
import com.zjucsc.tshark.packets.IEC104Packet;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import com.zjucsc.tshark.pre_processor.SinglePreProcessor;

import java.util.ArrayList;
import java.util.List;


/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-14 - 21:41
 */
public class IEC104PreProcessor extends SinglePreProcessor<IEC104Packet> {
    @Override
    public FvDimensionLayer decode(IEC104Packet packetInstance) {
        return packetInstance.layers.setFrameProtocols(PACKET_PROTOCOL.IEC104);
    }

    @Override
    public String singleProtocolFilterField() {
        return "104apci or 104asdu";
    }

    @Override
    public Class<IEC104Packet> decodeType() {
        return IEC104Packet.class;
    }

    @Override
    public List<String> filterFields() {
        return new ArrayList<String>(){
            {
                add("104asdu.typeid");
            }
        };
    }

}
