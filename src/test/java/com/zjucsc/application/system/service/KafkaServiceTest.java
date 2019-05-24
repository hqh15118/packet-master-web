package com.zjucsc.application.system.service;

import com.zjucsc.application.domain.bean.LogBean;
import com.zjucsc.application.system.service.impl.KafkaServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class KafkaServiceTest {

    @Autowired private KafkaServiceImpl kafkaServiceImpl;
    @Autowired private KafkaTemplate kafkaTemplate;

    @Test
    public void sendMsgTest(){
        LogBean logBean = LogBean.builder()
                .costTime(1000)
                .clazzName("clazz_name")
                .methodName("method_name")
                .logType(1)
                .methodArgs(new Object[]{1,2,3})
                .result("result")
                .exception("exception")
                .build();
        kafkaServiceImpl.sendImportLog(logBean);
        long time1 = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            kafkaTemplate.send("test","test_data");
        }
        System.out.println(System.currentTimeMillis() - time1);
    }

}