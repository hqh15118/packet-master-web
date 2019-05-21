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
        ListenableFuture<SendResult<String, String>> resultListenableFuture =  kafkaTemplate.send("test",0,"test_data" , "test_data11");
        resultListenableFuture.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable throwable) {
                System.out.println(throwable);
            }

            @Override
            public void onSuccess(SendResult<String, String> stringStringSendResult) {
                System.out.println("send successfully");
                System.out.println(stringStringSendResult);
            }
        });
    }

    @Override
    public void sendAllPacket(FvDimensionLayer layer) {

    }

    @Override
    public void sendStatisticsData(StatisticsDataWrapper wrapper) {

    }
}
