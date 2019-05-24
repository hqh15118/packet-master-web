package com.zjucsc.application.system.service.impl;

import com.zjucsc.application.config.KafkaConfig;
import com.zjucsc.application.domain.bean.LogBean;
import com.zjucsc.application.domain.bean.StatisticsDataWrapper;
import com.zjucsc.application.system.service.iservice.IKafkaService;
import com.zjucsc.application.tshark.domain.packet.FvDimensionLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
@SuppressWarnings("unchecked")
public class KafkaServiceImpl implements IKafkaService {
    @Autowired private KafkaTemplate kafkaTemplate;

    @Async
    @Override
    public void sendAllPacket(FvDimensionLayer layer) {

    }

    @Async
    @Override
    public void sendStatisticsData(StatisticsDataWrapper wrapper) {

    }

    @Async
    @Override
    public void sendImportLog(LogBean logBean) {
        kafkaTemplate.send(KafkaConfig.SEND_NORMAL_LOG,logBean);
    }

    @Async
    @Override
    public void sendNormalLog(LogBean logBean) {
        kafkaTemplate.send(KafkaConfig.SEND_IMPORTANT_LOG,logBean);
    }
}
