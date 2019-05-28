package com.zjucsc.application.system.service.iservice;

import com.zjucsc.application.domain.bean.LogBean;
import com.zjucsc.application.domain.bean.StatisticsDataWrapper;
import com.zjucsc.application.tshark.domain.bean.BadPacket;
import com.zjucsc.tshark.packets.FvDimensionLayer;

public interface IKafkaService {

    void sendAllPacket(FvDimensionLayer layer);
    void sendStatisticsData(StatisticsDataWrapper wrapper);

    void sendImportLog(LogBean logBean);
    void sendNormalLog(LogBean logBean);

    void sendExceptionPacket(BadPacket badPacket);

    void sendAttackPacket(BadPacket badPacket);
}
