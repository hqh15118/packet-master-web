package com.zjucsc.application.tshark.pre_processor;

import com.zjucsc.application.tshark.domain.packet.Dnp3_0Packet;
import com.zjucsc.application.tshark.domain.packet.FvDimensionLayer;

import java.util.Arrays;
import java.util.List;

import static com.zjucsc.application.util.PacketDecodeUtil.discernPacket;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-14 - 21:30
 */
public class DNP3_0PreProcessor extends BasePreProcessor<Dnp3_0Packet> {
    @Override
    public FvDimensionLayer decode(Dnp3_0Packet packetInstance) {
        return packetInstance.layers.setFrameProtocols(
                discernPacket(packetInstance.layers.frame_protocols[0]));
    }

    @Override
    public String protocolFilterField() {
        return "dnp3";
    }

    @Override
    public Class<Dnp3_0Packet> decodeType() {
        return Dnp3_0Packet.class;
    }

    @Override
    public List<String> filterFields() {
        return Arrays.asList(
                "dnp3.ctl.dir",
                "dnp3.ctl.prm",
                "dnp3.ctl.dfc"
        );
    }
}