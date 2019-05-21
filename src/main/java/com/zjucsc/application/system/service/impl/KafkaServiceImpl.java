package com.zjucsc.application.system.service.impl;

import com.zjucsc.application.domain.bean.StatisticsDataWrapper;
import com.zjucsc.application.system.service.iservice.IKafkaService;
import com.zjucsc.application.tshark.domain.packet.FvDimensionLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class KafkaServiceImpl implements IKafkaService {
    @Autowired private KafkaTemplate kafkaTemplate;

    @SuppressWarnings("unchecked")
    public void sendMsg(){
        kafkaTemplate.send("test","fnhafoiabovga");
        kafkaTemplate.flush();
    }

    @Override
    public void sendAllPacket(FvDimensionLayer layer) {

    }

    @Override
    public void sendStatisticsData(StatisticsDataWrapper wrapper) {

    }
}
