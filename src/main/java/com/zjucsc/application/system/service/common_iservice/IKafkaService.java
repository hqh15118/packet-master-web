package com.zjucsc.application.system.service.common_iservice;

import com.zjucsc.application.domain.bean.FvDimensionWrapper;
import com.zjucsc.application.domain.bean.LogBean;
import com.zjucsc.application.domain.bean.StatisticsDataWrapper;
import com.zjucsc.application.tshark.domain.bean.BadPacket;

public interface IKafkaService {

    void sendAllPacket(FvDimensionWrapper layer);
    void sendStatisticsData(StatisticsDataWrapper wrapper);

    void sendImportLog(LogBean logBean);
    void sendNormalLog(LogBean logBean);

    void sendExceptionPacket(BadPacket badPacket);

    void sendAttackPacket(BadPacket badPacket);
}
