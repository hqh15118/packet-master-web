package com.zjucsc.packetmasterweb.spring;

import com.zjucsc.application.system.service.PcapMainService;
import com.zjucsc.application.tshark.capture.AbstractPacketService;
import com.zjucsc.application.util.PcapUtils;
import org.junit.Test;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-04 - 22:38
 */
public class PcapTest {
    
    @Test
    public void pcap_service_test() throws InterruptedException {
        PcapMainService service = new PcapMainService();
        service.start("en0","tcp", new AbstractPacketService.ProcessCallback() {
            @Override
            public void error(Exception e) {
                System.out.println(e);
            }

            @Override
            public void start() {
                System.out.println("start");
            }

            @Override
            public void end(Object... objs) {
                System.out.println("end");
            }
        });
        Thread.sleep(10000);
        service.stop();
        Thread.sleep(100000000);
    }

    @Test
    public void decodeName(){
        for (String s : PcapUtils.getAllNetworkInterfaceDetailInfo()) {
            System.out.println(s);
        }
        System.out.println(PcapUtils.getTargetNetworkInterfaceName("192.168.0.121"));
    }
}
