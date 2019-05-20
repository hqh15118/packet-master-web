package com.zjucsc.application.system.service;

import com.zjucsc.application.config.SocketIoEvent;
import com.zjucsc.application.domain.bean.StatisticsDataWrapper;
import com.zjucsc.application.socketio.SocketServiceCenter;
import com.zjucsc.application.tshark.capture.CapturePacketService;
import com.zjucsc.application.tshark.capture.CapturePacketServiceImpl;
import com.zjucsc.application.tshark.capture.NewFvDimensionCallback;
import com.zjucsc.application.tshark.domain.packet.FvDimensionLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import static com.zjucsc.application.config.StatisticsData.*;

@Service
public class ScheduledService {

    @Autowired public PacketAnalyzeService packetAnalyzeService;

    private volatile boolean hasSend = false;

    private LinkedBlockingQueue<FvDimensionLayer> fvDimensionLayers = new LinkedBlockingQueue<>(5);

    @Autowired
    public ScheduledService(CapturePacketService capturePacketService){
        capturePacketService.setNewFvDimensionCallback(layer -> {
            fvDimensionLayers.offer(layer);
        });
    }

    /**
     * 5 秒钟发送一次统计信息，发送总报文数、总流量
     */
    @Scheduled(fixedRate = 5000)
    public void sendPacketStatisticsMsg(){
        Map<String,Integer> delayInfo = packetAnalyzeService.getCollectorNumToDelayList();
        SocketServiceCenter.updateAllClient(SocketIoEvent.STATISTICS_PACKET,
                new StatisticsDataWrapper.Builder()
                .setCollectorDelay(delayInfo)
                .setAttackByDevice(ATTACK_BY_DEVICE)    //
                .setExceptionByDevice(EXCEPTION_BY_DEVICE)
                .setAttackCount(attackNumber.get()) //攻击总数
                .setExceptionCount(exceptionNumber.get())   //异常总数
                .setNumber(recvPacketNumber.get())      //捕获的总报文数
                .setNumberByDeviceIn(NUMBER_BY_DEVICE_IN)
                .setNumberByDeviceOut(NUMBER_BY_DEVICE_OUT)
                .build()
                );
    }

    @Scheduled(fixedRate = 1000)
    public void sendAllFvDimensionPacket(){
        for (int i = 0; i < 5; i++) {
            doSend(fvDimensionLayers.poll());
        }
    }

    private void doSend(FvDimensionLayer layer){
        if (layer!=null){
            SocketServiceCenter.updateAllClient(SocketIoEvent.ALL_PACKET,layer);
        }
    }

}
