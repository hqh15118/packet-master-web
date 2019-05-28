package com.zjucsc.application.system.service.impl;

import com.zjucsc.application.domain.bean.LogBean;
import com.zjucsc.application.domain.bean.StatisticsDataWrapper;
import com.zjucsc.application.system.service.iservice.IKafkaService;
import com.zjucsc.application.tshark.domain.bean.BadPacket;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("unchecked")
public class KafkaServiceImpl implements IKafkaService {


    @Override
    public void sendAllPacket(FvDimensionLayer layer) {
    }

    @Override
    public void sendStatisticsData(StatisticsDataWrapper wrapper) {

    }

    @Override
    public void sendImportLog(LogBean logBean) {

    }

    @Override
    public void sendNormalLog(LogBean logBean) {

    }

    @Override
    public void sendExceptionPacket(BadPacket badPacket) {

    }

    @Override
    public void sendAttackPacket(BadPacket badPacket) {

    }
}
