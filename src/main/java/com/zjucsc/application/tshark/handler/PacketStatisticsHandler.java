package com.zjucsc.application.tshark.handler;

import com.zjucsc.application.system.service.PacketAnalyzeService;
import com.zjucsc.application.tshark.decode.AbstractAsyncHandler;
import com.zjucsc.application.tshark.domain.packet.FiveDimensionPacketWrapper;

import java.util.concurrent.ExecutorService;


public class PacketStatisticsHandler extends AbstractAsyncHandler<Void> {

    public PacketAnalyzeService packetAnalyzeService;

    public PacketStatisticsHandler(PacketAnalyzeService packetAnalyzeService , ExecutorService executor) {
        super(executor);
        this.packetAnalyzeService = packetAnalyzeService;
    }

    @Override
    public Void handle(Object t) {
        FiveDimensionPacketWrapper wrapper = ((FiveDimensionPacketWrapper) t);
        //System.out.println("statis packet handler : " + wrapper );
        packetAnalyzeService.addPacketFlow(Integer.parseInt(wrapper.packetLength));
        packetAnalyzeService.addPacketNumber();
        return null;
    }
}
