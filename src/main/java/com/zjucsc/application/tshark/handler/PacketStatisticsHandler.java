package com.zjucsc.application.tshark.handler;

import com.zjucsc.application.config.Common;
import com.zjucsc.application.system.service.impl.PacketAnalyzeService;
import com.zjucsc.application.tshark.decode.AbstractAsyncHandler;
import com.zjucsc.application.tshark.domain.beans.PacketInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class PacketStatisticsHandler extends AbstractAsyncHandler<Void> {

    public PacketAnalyzeService packetAnalyzeService;

    public PacketStatisticsHandler(PacketAnalyzeService packetAnalyzeService , ExecutorService executor) {
        super(executor);
        this.packetAnalyzeService = packetAnalyzeService;
    }

    @Override
    public Void handle(Object t) {
        PacketInfo.PacketWrapper wrapper = ((PacketInfo.PacketWrapper) t);
        //System.out.println("statis packet handler : " + wrapper );
        packetAnalyzeService.addPacketFlow(Integer.parseInt(wrapper.packetLength));
        packetAnalyzeService.addPacketNumber();

        return null;
    }
}
