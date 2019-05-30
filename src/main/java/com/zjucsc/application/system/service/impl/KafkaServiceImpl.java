package com.zjucsc.application.system.service.impl;

import com.zjucsc.application.domain.bean.LogBean;
import com.zjucsc.application.domain.bean.StatisticsDataWrapper;
import com.zjucsc.application.system.service.iservice.IKafkaService;
import com.zjucsc.application.tshark.domain.bean.BadPacket;
<<<<<<< HEAD
import com.zjucsc.application.tshark.domain.packet.FvDimensionLayer;
=======
import com.zjucsc.tshark.packets.FvDimensionLayer;
>>>>>>> 4843672329fddc0e7fc95a1155232941dac9b7c5
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
<<<<<<< HEAD
=======

>>>>>>> 4843672329fddc0e7fc95a1155232941dac9b7c5
    }

    @Override
    public void sendNormalLog(LogBean logBean) {
<<<<<<< HEAD
=======

>>>>>>> 4843672329fddc0e7fc95a1155232941dac9b7c5
    }

    @Override
    public void sendExceptionPacket(BadPacket badPacket) {

    }

    @Override
    public void sendAttackPacket(BadPacket badPacket) {

    }
}
