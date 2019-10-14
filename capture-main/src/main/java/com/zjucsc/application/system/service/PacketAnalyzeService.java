package com.zjucsc.application.system.service;

import com.zjucsc.application.system.service.hessian_iservice.IDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

import static com.zjucsc.application.statistic.StatisticsData.COLLECTOR_DELAY_MAP;

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
        COLLECTOR_DELAY_MAP.compute(collectorId, (collectorId1, lastDelay) -> {
            if (lastDelay == null){
                return delay;
            }else{
                return delay + lastDelay;
            }
        });
    }

    private HashMap<String,Integer> collectorNumToDelayMap = new HashMap<>(20);

    public synchronized HashMap<String,Integer> getCollectorNumToDelayList(){
        collectorNumToDelayMap.clear();
        COLLECTOR_DELAY_MAP.forEach((collectorId, delay) -> {
//            String deviceNumber = iDeviceService.selectDeviceNumberByCollectorTag(String.valueOf(collectorId) , Common.GPLOT_ID);
//            if (deviceNumber!=null){
//                collectorNumToDelayMap.put(deviceNumber,delay);
//            }
            collectorNumToDelayMap.put(String.valueOf(collectorId),delay);
        });
        clearCollectorDelay();                      //清除旧的时延数据
        return collectorNumToDelayMap;
    }

    /**
     * 更换拓扑时需要清空当前的时延数据
     */
    public void clearCollectorDelay(){
        //TODO 是否需要存储时延数据
        COLLECTOR_DELAY_MAP.clear();
    }
}
