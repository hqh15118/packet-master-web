package com.zjucsc.packetmasterweb.spring;

import com.alibaba.fastjson.JSON;
import com.zjucsc.application.system.controller.UserOptController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

    @Autowired private UserOptController userOptController;

    @Test
    public void getUserInfo() throws InterruptedException {
        //
        System.out.println(JSON.toJSONString(userOptController.getUseInfo("c5044a18839c2e553c91b5dfa0caa397_token")));
        System.out.println(JSON.toJSONString(userOptController.getAllLogginedUser()));
        Thread.sleep(100000000);
    }


}
