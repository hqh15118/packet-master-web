package com.zjucsc.packetmasterweb.spring;


import com.zjucsc.application.domain.bean.CaptureService;
import com.zjucsc.application.domain.exceptions.DeviceNotValidException;
import com.zjucsc.application.system.controller.PacketController;
import com.zjucsc.application.system.service.iservice.PacketService;
import com.zjucsc.application.tshark.capture.CapturePacketService;
import com.zjucsc.application.tshark.capture.CapturePacketServiceImpl;
import com.zjucsc.application.tshark.capture.ProcessCallback;
import com.zjucsc.packetmasterweb.util.Util;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.SocketException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PacketControllerTest {

    @Autowired private PacketController packetController;
    @Autowired private CapturePacketServiceImpl capturePacketService;

    @Test
    public void getAllDevice() throws SocketException {
        Util.showJSON("all devices" , packetController.getAllNetworkInterfaces());
    }

    @Test
    public void startCapture() throws InterruptedException {
        //modbus set -c 1 ok
        //s7 set -c 2   ok
        capturePacketService.start(new ProcessCallback<String, String>() {
            @Override
            public void error(Exception e) {

            }

            @Override
            public void start(String start) {
                System.out.println("start : ");
                System.out.println(start);
            }

            @Override
            public void end(String end) {

            }
        });
        Thread.sleep(10000);
    }



}
