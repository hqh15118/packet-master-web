package com.zjucsc.application.system.service;

import com.zjucsc.application.config.Common;
import com.zjucsc.application.system.service.iservice.IDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.function.BiConsumer;

import static com.zjucsc.application.config.Common.COLLECTOR_DELAY_MAP;
import static com.zjucsc.application.config.Common.recvPacketFlow;
import static com.zjucsc.application.config.Common.recvPacketNuber;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-04-30 - 22:34
 */
@Service
@Slf4j
public class PacketAnalyzeService {

    @Autowired private IDeviceService iDeviceService;

    public void addPacketNumber(){
        recvPacketNuber++;
    }

    public void addPacketFlow(int packetFlowLength){
        recvPacketFlow += packetFlowLength;
    }

    public long getRecvPacketNumber(){
        return recvPacketNuber;
    }

    public long getRecvPacketFlow(){
        return recvPacketFlow;
    }

    public void setCollectorDelay(int collectorId , int delay){
        if (Common.COLLECTOR_DELAY_MAP.get(collectorId) < delay){
            COLLECTOR_DELAY_MAP.put(collectorId , delay);
            log.info("update collector id {} delay {}" , collectorId , delay);
        }
    }

    private HashMap<String,Integer> collectorNumToDelayMap = new HashMap<>();

    public synchronized HashMap<String,Integer> getCollectorNumToDelayMap(){
        collectorNumToDelayMap.clear();
        Common.COLLECTOR_DELAY_MAP.forEach(new BiConsumer<Integer, Integer>() {
            @Override
            public void accept(Integer integer, Integer aLong) {
                String collectorNumber = iDeviceService.selectDeviceNumberByCollectorTag(String.valueOf(integer));
                if (collectorNumber!=null){
                    collectorNumToDelayMap.put(collectorNumber,aLong);
                }else{
                    log.error("*********************\n 无法找到采集器ID为 {} 的设备号(device_number)" , integer);
                }
            }
        });
        return collectorNumToDelayMap;
    }

    /**
     * 更换拓扑时需要清空当前的时延数据
     */
    public void clearCollectorDelay(){
        //TODO 是否需要存储时延数据？
        Common.COLLECTOR_DELAY_MAP.clear();
    }
}
