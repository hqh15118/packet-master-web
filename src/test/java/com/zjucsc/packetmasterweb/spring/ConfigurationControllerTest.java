package com.zjucsc.packetmasterweb.spring;

import com.alibaba.fastjson.JSON;
import com.zjucsc.application.domain.exceptions.DeviceNotValidException;
import com.zjucsc.application.system.controller.ConfigurationController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfigurationControllerTest {

    @Autowired private ConfigurationController configurationController;

}
