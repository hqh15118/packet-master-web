package com.zjucsc;

import com.zjucsc.application.config.Common;
import com.zjucsc.application.controller.PacketController;
import com.zjucsc.application.util.PacketDecodeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


//@RunWith(SpringRunner.class)
//@SpringBootTest
public class PacketMasterWebApplicationTest {
    @Autowired
    private PacketController packetController;

    @Test
    public void simulateTest() throws InterruptedException {
        Common.systemRunType = 0;
        packetController.startCaptureService(null);
        Thread.sleep(5000);
        packetController.stopService(null);
        System.out.println("******stop*******");
        Thread.sleep(5000);
        packetController.startCaptureService(null);
        System.out.println("******start*******");
        Thread.sleep(10000);
    }

    @Test
    public void test(){
        String http = "eth : mms : tcp : http";
        System.out.println(PacketDecodeUtil.getUnDefinedPacketProtocol(http));
    }

}