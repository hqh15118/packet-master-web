package com.zjucsc.application.tshark.packet;

import com.zjucsc.application.domain.bean.BadPacket;
import com.zjucsc.application.tshark.BasePacketHandler;
import com.zjucsc.application.tshark.decode.AbstractAsyncHandler;
import com.zjucsc.application.tshark.decode.AbstractHandler;
import com.zjucsc.application.tshark.decode.IPacketAnalyzer;
import com.zjucsc.application.tshark.decode.PipeLine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-04-29 - 23:11
 */
public class PacketAnalyzeHandler extends AbstractAsyncHandler<BadPacket> {

    private List<IPacketAnalyzer> packetAnalyzers = new ArrayList<>();
    @Override
    public BadPacket handle(Object t) {

        return null;
    }



    public void addPacketAnalyzer(IPacketAnalyzer iPacketAnalyzer){
        packetAnalyzers.add(iPacketAnalyzer);
    }
}
