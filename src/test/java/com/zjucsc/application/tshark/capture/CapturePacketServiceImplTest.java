package com.zjucsc.application.tshark.capture;

import com.zjucsc.application.tshark.pre_processor.BasePreProcessor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-13 - 20:34
 */

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class CapturePacketServiceImplTest {

    @Test
    public void start() throws InterruptedException {
        String macAddressForMac = "8c:85:90:93:15:a2";
        String deviceName = "en0";
        String macAddressForWin = "28:D2:44:5F:69:E1";
        CapturePacketServiceImpl capturePacketService = new CapturePacketServiceImpl();
        BasePreProcessor.setCaptureDeviceNameAndMacAddress(macAddressForWin,deviceName);
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

        Thread.sleep(10000000);
    }

    @Test
    public void stop() {
    }
}