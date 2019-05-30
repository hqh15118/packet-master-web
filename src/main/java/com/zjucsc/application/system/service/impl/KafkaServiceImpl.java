package com.zjucsc.application.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.zjucsc.application.config.KafkaConfig;
import com.zjucsc.application.domain.bean.LogBean;
import com.zjucsc.application.domain.bean.StatisticsDataWrapper;
import com.zjucsc.application.system.service.iservice.IKafkaService;
import com.zjucsc.application.tshark.domain.bean.BadPacket;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
@SuppressWarnings("unchecked")
public class KafkaServiceImpl implements IKafkaService {


    @Autowired private KafkaTemplate kafkaTemplate;

    @Override
    public void sendAllPacket(FvDimensionLayer layer) {
        String str = JSON.toJSONString(layer);
        ListenableFuture<SendResult> listenableFuture =  kafkaTemplate.send(KafkaConfig.SEND_ALL_PACKET_FV_DIMENSION,str);
        listenableFuture.addCallback(new ListenableFutureCallback<SendResult>() {
            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("fail");
            }

            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("successfully");
                System.out.println(sendResult);
            }
        });
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
