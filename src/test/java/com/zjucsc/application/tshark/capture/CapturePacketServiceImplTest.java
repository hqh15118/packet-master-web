package com.zjucsc.application.tshark.capture;

import com.zjucsc.application.domain.bean.CaptureService;
import com.zjucsc.application.system.controller.DeviceController;
import com.zjucsc.application.system.controller.PacketController;
import com.zjucsc.application.system.service.iservice.PacketService;
import com.zjucsc.application.tshark.pre_processor.BasePreProcessor;
import com.zjucsc.application.util.CommonCacheUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Scanner;

import static org.junit.Assert.*;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-13 - 20:34
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class CapturePacketServiceImplTest {

    private String macAddressForWin = "28:D2:44:5F:69:E1";
    private String macAddressForMac = "8c:85:90:93:15:a2";

    @Test
    public void start() throws InterruptedException {
        String deviceName = "en0";
        CapturePacketServiceImpl capturePacketService = new CapturePacketServiceImpl();
        BasePreProcessor.setCaptureDeviceNameAndMacAddress(macAddressForWin,deviceName);
        for (int i = 0; i < 10; i++) {
            capturePacketService.start(new ProcessCallback<String, String>() {
                @Override
                public void error(Exception e) {

                }
                @Override
                public void start(String start) {
                    System.out.println(start);
                }

                @Override
                public void end(String end) {

                }
            });
            Thread.sleep(20000);
            capturePacketService.stop();
        }
    }

    @Test
    public void stop() {
    }

    @Autowired private PacketController packetController;

    @Test
    @SuppressWarnings("unchecked")
    public void allPacketSendTest() throws InterruptedException {
        String ipForWin = "192.168.0.121";
        String ipForMac = "192.168.1.102";
        //packetController.startRecvRealTimePacket();
        CaptureService captureService = new CaptureService();
        captureService.setMacAddress(macAddressForWin);
        captureService.setService_ip(ipForMac);
        captureService.setService_name("en0");
        packetController.startCaptureService(captureService);
        Thread.sleep(5000);
    }
}