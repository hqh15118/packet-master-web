package com.zjucsc.application.system.service;

import com.zjucsc.application.config.Common;
import com.zjucsc.application.config.SocketIoEvent;
import com.zjucsc.application.config.StatisticsData;
import com.zjucsc.application.domain.bean.CollectorDelay;
import com.zjucsc.application.domain.bean.StatisticsDataWrapper;
import com.zjucsc.application.socketio.SocketServiceCenter;
import com.zjucsc.application.system.service.impl.PacketServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zjucsc.application.config.StatisticsData.*;

@Service
public class ScheduledService {

    @Autowired public PacketAnalyzeService packetAnalyzeService;

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

}
