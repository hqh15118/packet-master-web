package com.zjucsc.packetmasterweb.spring;


import com.zjucsc.application.domain.bean.CaptureService;
import com.zjucsc.application.domain.exceptions.DeviceNotValidException;
import com.zjucsc.application.system.controller.PacketController;
import com.zjucsc.application.system.service.iservice.PacketService;
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

    @Test
    public void getAllDevice() throws SocketException {
        Util.showJSON("all devices" , packetController.getAllNetworkInterfaces());
    }

    @Test
    public void startCapture() throws InterruptedException, DeviceNotValidException {
        CaptureService service = new CaptureService();
        service.setService_name("\\Device\\NPF_{8E98ECE7-BD55-4074-B5B8-2D357F85491A}");
        service.setService_ip("192.168.0.121");
        packetController.startCaptureService(service);
        Thread.sleep(10000);
    }

    @Test
    public void stopCapture() throws InterruptedException, DeviceNotValidException {
        startCapture();
        CaptureService service = new CaptureService();
        service.setService_name("\\Device\\NPF_{8E98ECE7-BD55-4074-B5B8-2D357F85491A}");
        //service.setService_ip("192.168.0.121");
        packetController.stopService(service);
        Thread.sleep(10000000);
    }
}
