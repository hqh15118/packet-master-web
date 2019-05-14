package com.zjucsc.application.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-14 - 22:56
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ConstantConfigTest {

    @Autowired private ConstantConfig constantConfig;

    @Test
    public void configTest(){
        System.out.println(constantConfig.getGlobal_address());
    }
}