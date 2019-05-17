package com.zjucsc.application.system.service;

import com.zjucsc.application.domain.bean.CollectorDelay;
import com.zjucsc.application.system.service.iservice.IDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import static com.zjucsc.application.config.StatisticsData.*;

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

    public void setCollectorDelay(int collectorId , int delay){
        if (COLLECTOR_DELAY_MAP.get(collectorId) < delay){
            COLLECTOR_DELAY_MAP.put(collectorId , delay);
            log.info("update collector id {} delay {}" , collectorId , delay);
        }
    }

    private Map<String,Integer> collectorNumToDelayMap = new HashMap<>(20);

    public synchronized Map<String,Integer> getCollectorNumToDelayList(){
        collectorNumToDelayMap.clear();
        COLLECTOR_DELAY_MAP.forEach(new BiConsumer<Integer, Integer>() {
            @Override
            public void accept(Integer integer, Integer delay) {
                String deviceNumber = iDeviceService.selectDeviceNumberByCollectorTag(String.valueOf(integer));
                if (deviceNumber!=null){
                    collectorNumToDelayMap.put(deviceNumber,delay);
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
        COLLECTOR_DELAY_MAP.clear();
    }
}
