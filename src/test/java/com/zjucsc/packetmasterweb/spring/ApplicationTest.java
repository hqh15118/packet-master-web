package com.zjucsc.packetmasterweb.spring;

import com.zjucsc.application.config.Common;
import com.zjucsc.application.domain.entity.FiveDimensionFilterEntity;
import com.zjucsc.application.domain.entity.User;
import com.zjucsc.application.system.controller.ConfigurationController;
import com.zjucsc.application.system.controller.PacketController;
import com.zjucsc.application.system.controller.UserOptController;
import com.zjucsc.application.system.service.impl.TsharkMainService;
import com.zjucsc.application.tshark.capture.AbstractPacketService;
import com.zjucsc.application.tshark.domain.packet.FiveDimensionPacket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static com.zjucsc.application.config.PACKET_PROTOCOL.MODBUS;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-03 - 22:19
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {

    @Autowired private TsharkMainService tsharkMainService;

    @Test
    public void fv_dimension_packet_filter_test(){

    }

    @Test
    public void operation_packet_filter_test() throws InterruptedException {
        tsharkMainService.start(Common.CAPTURE_COMMAND_MAC);
        Thread.sleep(30000000);
    }

    public static final String CAPTURE_COMMAND_MAC_FV = "/Applications/Wireshark.app/Contents/MacOS/tshark -l -n -Y tcp -e frame.protocols -e eth.dst -e frame.cap_len -e eth.src -e ip.src -e ip.dst -e tcp.srcport -e tcp.dstport -e s7comm.param.func -e modbus.func_code -e tcp.payload -e s7comm.header.rosctr -T ek -r /Users/hongqianhui/JavaProjects/packet-master-web/src/main/resources/pcap4j/question_1531953261_01.pcap4j";

    @Autowired private ConfigurationController configurationController;
    @Autowired private UserOptController userOptController;
    @Autowired private PacketController packetController;

    @Test
    public void login(){
        User user = new User();
        user.setName("hongqianhui");
        user.setPassword("123");
        System.out.println(userOptController.login(user));;
    }

    @Test
    public void socketServer() throws InterruptedException {
        packetController.startRecvRealTimePacket();
        Thread.sleep(30000000);
    }

    @Test
    public void fv_packet_filter_test() throws InterruptedException {
        packetController.startRecvRealTimePacket();
        login();
        Thread.sleep(5000);
        FiveDimensionFilterEntity.FiveDimensionFilter filter1 = new FiveDimensionFilterEntity.FiveDimensionFilter();
        filter1.setFilterType(1);
        filter1.setSrc_ip("192.168.254.134");
        filter1.setDst_port("502");
        filter1.setSrc_port("1075");
        filter1.setProtocol(MODBUS);
        filter1.setDst_ip("192.168.254.143");
        FiveDimensionFilterEntity.FiveDimensionFilterForFront fiveDimensionFilterForFront
                = new FiveDimensionFilterEntity.FiveDimensionFilterForFront();
        fiveDimensionFilterForFront.setFiveDimensionFilters(Arrays.asList(
                filter1
        ));
        fiveDimensionFilterForFront.setUserName("hongqianhui");
        System.out.println(configurationController.configFvDimensionPacketRule(fiveDimensionFilterForFront));
        tsharkMainService.start(CAPTURE_COMMAND_MAC_FV);
        Thread.sleep(30000000);
    }

    /**
     *  这里的到的包数和wireshark得到的包数不同，因为这里是过滤掉非TCP的包的
     * @throws InterruptedException
     */
    @Test
    public void all_packet_number() throws InterruptedException {
        tsharkMainService.start(CAPTURE_COMMAND_MAC_FV, new AbstractPacketService.ProcessCallback() {
            @Override
            public void error(Exception e) {

            }

            @Override
            public void start() {

            }

            @Override
            public void end(Object...objects) {
                System.out.println((int)objects[0]);
            }
        });
        Thread.sleep(30000000);
    }

}
