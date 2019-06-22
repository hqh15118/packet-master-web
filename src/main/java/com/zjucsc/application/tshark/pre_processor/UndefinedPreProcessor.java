package com.zjucsc.application.tshark.pre_processor;

import com.zjucsc.tshark.packets.UndefinedPacket;
import com.zjucsc.tshark.CommonTsharkUtil;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import com.zjucsc.tshark.pre_processor.SinglePreProcessor;

import java.util.ArrayList;
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
public class UndefinedPreProcessor extends SinglePreProcessor<UndefinedPacket> {

    @Override
    public FvDimensionLayer decode(UndefinedPacket packetInstance) {
        //protocol_stack
        return packetInstance.layers.setFrameProtocols(
                discernPacket(packetInstance.layers.frame_protocols[0]));
    }

    @Override
    public String singleProtocolFilterField() {
        StringBuilder sb = new StringBuilder();
        Set<String> captureProtocolSet = CommonTsharkUtil.getCaptureProtocols();
        int i = 0;
        int setSize = captureProtocolSet.size();
        for (String s : captureProtocolSet) {
            if (i < setSize - 1) {
                sb.append(" not ").append(s).append(" and");
            }else{
                sb.append(" not ").append(s);
            }
            i++;
        }
        return sb.toString();
    }

    @Override
    public Class<UndefinedPacket> decodeType() {
        return UndefinedPacket.class;
    }

    @Override
    public List<String> filterFields() {
        return new ArrayList<String>(){
            {

            }
        };
    }

    @Override
    public String filter() {
        return "";
    }
}
