package com.zjucsc.application.system.service;

import com.zjucsc.application.config.Common;
import com.zjucsc.application.config.SocketIoEvent;
import com.zjucsc.application.socketio.SocketServiceCenter;
import com.zjucsc.application.system.service.impl.PacketServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ScheduledService {

    @Autowired public PacketAnalyzeService packetAnalyzeService;

    /**
     * 10秒钟发送一次统计信息，发送总报文数、总流量
     */
    @Scheduled(fixedRate = 10000)
    public void sendPacketStatisticsMsg(){
        HashMap<String,Integer> delayInfo = packetAnalyzeService.getCollectorNumToDelayMap();
        SocketServiceCenter.updateAllClient(SocketIoEvent.STATISTICS_PACKET,new PacketServiceImpl.StatisticsDataWrapper(packetAnalyzeService.getRecvPacketNumber()
                ,packetAnalyzeService.getRecvPacketFlow(),0 , Common.exceptionCound, delayInfo));
    }

}
