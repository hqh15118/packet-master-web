package com.zjucsc.application.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.zjucsc.application.config.KafkaConfig;
import com.zjucsc.application.domain.bean.LogBean;
import com.zjucsc.application.domain.bean.StatisticsDataWrapper;
import com.zjucsc.application.system.service.iservice.IKafkaService;
import com.zjucsc.application.tshark.domain.bean.BadPacket;
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

    @Override
    public void sendAllPacket(FvDimensionLayer layer) {
        kafkaTemplate.send(KafkaConfig.SEND_ALL_PACKET_FV_DIMENSION,layer);
    }

    @Override
    public void sendStatisticsData(StatisticsDataWrapper wrapper) {

    }

    @Override
    public void sendImportLog(LogBean logBean) {
        kafkaTemplate.send(KafkaConfig.SEND_NORMAL_LOG, JSON.toJSONString(logBean));
    }

    @Override
    public void sendNormalLog(LogBean logBean) {
        kafkaTemplate.send(KafkaConfig.SEND_IMPORTANT_LOG,JSON.toJSONString(logBean));
    }

    @Override
    public void sendExceptionPacket(BadPacket badPacket) {

    }

    @Override
    public void sendAttackPacket(BadPacket badPacket) {

    }
}
