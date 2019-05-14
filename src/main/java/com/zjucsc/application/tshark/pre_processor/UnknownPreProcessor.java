package com.zjucsc.application.tshark.pre_processor;

import com.zjucsc.application.tshark.domain.packet.FvDimensionLayer;
import com.zjucsc.application.tshark.domain.packet.UnknownPacket;
import com.zjucsc.application.util.CommonTsharkUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.zjucsc.application.util.PacketDecodeUtil.discernPacket;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-14 - 20:32
 */

/**
 * 用于捕获除了必须报文之外的其他报文
 */
public class UnknownPreProcessor extends BasePreProcessor<UnknownPacket> {

    @Override
    public FvDimensionLayer decode(UnknownPacket packetInstance) {
        //protocol_stack
        return packetInstance.layers.setFrameProtocols(
                discernPacket(packetInstance.layers.frame_protocols[0]));
    }

    @Override
    public String protocolFilterField() {
        StringBuilder sb = new StringBuilder();
        Set<String> captureProtocolSet = CommonTsharkUtil.getCaptureProtocols();
        for (String s : captureProtocolSet) {
            sb.append(" not ").append(s) .append(" ");
        }
        return sb.toString();
    }

    @Override
    public Class<UnknownPacket> decodeType() {
        return UnknownPacket.class;
    }

    @Override
    public List<String> filterFields() {
        return new ArrayList<String>(){
            {

            }
        };
    }
}
