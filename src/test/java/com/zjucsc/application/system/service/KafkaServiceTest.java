package com.zjucsc.application.system.service;

import com.zjucsc.application.system.service.impl.KafkaServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class KafkaServiceTest {

    @Autowired private KafkaServiceImpl kafkaServiceImpl;


}