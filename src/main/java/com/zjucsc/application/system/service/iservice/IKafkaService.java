package com.zjucsc.application.system.service.iservice;

import com.zjucsc.application.domain.bean.StatisticsDataWrapper;
import com.zjucsc.application.tshark.domain.packet.FvDimensionLayer;

public interface IKafkaService {

    void sendAllPacket(FvDimensionLayer layer);
    void sendStatisticsData(StatisticsDataWrapper wrapper);
}
