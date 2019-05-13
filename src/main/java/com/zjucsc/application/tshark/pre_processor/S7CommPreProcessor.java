package com.zjucsc.application.tshark.pre_processor;

import com.zjucsc.application.tshark.domain.packet.FvDimensionLayer;
import com.zjucsc.application.tshark.domain.packet.S7CommPacket;

import java.util.ArrayList;
import java.util.List;

import static com.zjucsc.application.util.PacketDecodeUtil.discernPacket;

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

    private S7CommPacket.LayersBean layersBean;

    @Override
    public FvDimensionLayer decode(S7CommPacket packetInstance) {
        return packetInstance.layersX.setFrameProtocols(
                discernPacket(packetInstance.layersX.frame_protocols[0],
                        packetInstance.layersX.s7comm_header_rosctr[0]));
    }
}
