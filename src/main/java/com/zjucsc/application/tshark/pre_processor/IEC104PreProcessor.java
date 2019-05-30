package com.zjucsc.application.tshark.pre_processor;

import com.zjucsc.application.tshark.domain.packet.IEC104Packet;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import com.zjucsc.tshark.pre_processor.SinglePreProcessor;

import java.util.ArrayList;
import java.util.List;

import static com.zjucsc.application.util.PacketDecodeUtil.discernPacket;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-14 - 21:41
 */
public class IEC104PreProcessor extends SinglePreProcessor<IEC104Packet> {
    @Override
    public FvDimensionLayer decode(IEC104Packet packetInstance) {
        return packetInstance.layers.setFrameProtocols(
                discernPacket(packetInstance.layers.frame_protocols[0]));
    }

    @Override
    public String singleProtocolFilterField() {
        return "104apci";
    }

    @Override
    public Class<IEC104Packet> decodeType() {
        return IEC104Packet.class;
    }

    @Override
    public List<String> filterFields() {
        return new ArrayList<String>(){
            {
                add("104asdu.start");
                add("104apci.apdulen");
                add("104apci.type");
                add("104apci.utype");
            }
        };
    }

}
