package com.zjucsc.application.tshark.pre_processor;

import com.zjucsc.application.tshark.domain.packet.FvDimensionLayer;
import com.zjucsc.application.tshark.domain.packet.IEC104Packet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.zjucsc.application.util.PacketDecodeUtil.discernPacket;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-14 - 21:41
 */
public class IEC104PreProcessor extends BasePreProcessor<IEC104Packet> {
    @Override
    public FvDimensionLayer decode(IEC104Packet packetInstance) {
        return packetInstance.layers.setFrameProtocols(
                discernPacket(packetInstance.layers.frame_protocols[0]));
    }

    @Override
    public String protocolFilterField() {
        return "iec60870_104";
    }

    @Override
    public Class<IEC104Packet> decodeType() {
        return IEC104Packet.class;
    }

    @Override
    public List<String> filterFields() {
        return new ArrayList<String>(){
            {
                add("iec60870_asdu.start");
                add("iec60870_104.apdulen");
                add("iec60870_104.type");
                add("iec60870_104.utype");
            }
        };
    }

}
