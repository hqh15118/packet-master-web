package com.zjucsc.packetmasterweb.spring;

import com.alibaba.fastjson.JSON;
import com.zjucsc.application.domain.bean.CaptureService;
import com.zjucsc.application.system.entity.FvDimensionFilter;
import com.zjucsc.application.system.entity.OptFilter;
import com.zjucsc.application.system.entity.User;
import com.zjucsc.application.domain.exceptions.DeviceNotValidException;
import com.zjucsc.application.domain.exceptions.ProtocolIdNotValidException;
import com.zjucsc.application.system.controller.*;
import com.zjucsc.application.system.service.PcapMainService;
import com.zjucsc.application.system.service.TsharkMainService;
import com.zjucsc.application.tshark.capture.AbstractPacketService;
import com.zjucsc.application.tshark.capture.PacketMain;
import com.zjucsc.application.tshark.decode.DefaultPipeLine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import static com.zjucsc.application.config.Common.CAPTURE_COMMAND_WIN;
import static com.zjucsc.application.config.PACKET_PROTOCOL.MODBUS_ID;

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
    @Autowired private PacketController packetController;
    @Autowired private OptFilterController optFilterController;
    @Autowired private FvDimensionFilterController fvDimensionFilterController;

    @Test
    public void initSpringTest() throws ExecutionException, InterruptedException {

    }

    @Test
    public void startCaptureTest() throws DeviceNotValidException, InterruptedException {
        packetController.startRecvRealTimePacket();
        CaptureService service = new CaptureService();
        service.setService_ip("192.168.0.121");
        service.setService_name("\\Device\\NPF_{8E98ECE7-BD55-4074-B5B8-2D357F85491A}");
        packetController.startCaptureService(service);
        Thread.sleep(1000000000);
    }

    public static final String CAPTURE_COMMAND_WIN_FV = "C:\\Users\\Administrator\\Desktop\\tshark_min_win\\tshark.exe -l -n -Y tcp -e frame.protocols -e eth.dst -e frame.cap_len -e eth.src -e ip.src -e ip.dst -e tcp.srcport -e tcp.dstport -e s7comm.param.func -e modbus.func_code -e tcp.payload -e s7comm.header.rosctr -T ek -c 5 -r C:\\Users\\Administrator\\IdeaProjects\\packet-master-web\\src\\main\\resources\\pcap\\question_1531953261_01.pcap";

    public static final String CAPTURE_COMMAND_MAC_FV = "/Applications/Wireshark.app/Contents/MacOS/tshark -l -n -Y tcp -e frame.protocols -e eth.dst -e frame.cap_len -e eth.src -e ip.src -e ip.dst -e tcp.srcport -e tcp.dstport -e s7comm.param.func -e modbus.func_code -e tcp.payload -e s7comm.header.rosctr -T ek -r /Users/hongqianhui/JavaProjects/packet-master-web/src/main/resources/pcap4j/question_1531953261_01.pcap4j";

    @Autowired private ConfigurationSettingController configurationSettingController;
    @Autowired private UserOptController userOptController;

    /**
     * 登录测试
     */
    @Test
    public void login() throws InterruptedException {
        User.UserForFront user = new User.UserForFront();
        user.userName = "hongqianhui";
        user.password = "123";
        System.out.println(JSON.toJSONString(userOptController.login(user)));
        Thread.sleep(1000000);
    }

    /**
     * 开启websocket
     * @throws InterruptedException
     */
    @Test
    public void socketServer() throws InterruptedException {
        packetController.startRecvRealTimePacket();
        Thread.sleep(30000000);
    }

    /**
     * 测试五元组过滤效果
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @Test
    public void fv_packet_filter_test() throws InterruptedException, ExecutionException {
        packetController.startRecvRealTimePacket();
        Thread.sleep(5000);
        tsharkMainService.start(CAPTURE_COMMAND_WIN_FV);
        Thread.sleep(30000000);
    }




    /**
     * 功能码配置测试
     */
    @Test
    public void config_fun_code_filter_test() throws ProtocolIdNotValidException, InterruptedException, DeviceNotValidException, ExecutionException {
        //login();
        OptFilter filter1 = new OptFilter();
        OptFilter filter2 = new OptFilter();
        filter1.setFun_code(1);
        filter1.setFilterType(1);
        filter2.setFun_code(4);
        filter2.setFilterType(0);
        filter1.setDeviceId(10);
        filter2.setDeviceId(10);
        OptFilter.OptFilterForFront operationFilterForFront
                = new OptFilter.OptFilterForFront();
        operationFilterForFront.setProtocolId(MODBUS_ID);

        operationFilterForFront.setOptFilterList(Arrays.asList(
                filter1 , filter2
        ));
        //System.out.println(JSON.toJSON(optFilterController.addNewOptFilter(Collections.singletonList(operationFilterForFront))));
        Thread.sleep(1000);
        System.out.println("cached operation rules :");
        //System.out.println(JSON.toJSON(optFilterController.getOptFilter(10 , 0 , 0 , 1)));
        System.out.println("operation rules :");
        //System.out.println(JSON.toJSON(optFilterController.getOptFilter(10 , 0 , 1 , 1)));
        Thread.sleep(1000);
//        System.out.println("operation rules [not config device id]:");
//        System.out.println(JSON.toJSON(optFilterController.getOptFilter(11 , 0 , 0)));
//        System.out.println(JSON.toJSON(optFilterController.getOptFilter(11 , 0 , 1)));
    }

    @Test
    public void operation_packet_filter_test() throws InterruptedException, ProtocolIdNotValidException, DeviceNotValidException, ExecutionException {
        packetController.startRecvRealTimePacket();
        Thread.sleep(5000);
        config_fun_code_filter_test();      //配置功能码过滤器      S7_Ack_data 为黑名单
        tsharkMainService.start(CAPTURE_COMMAND_WIN);
        Thread.sleep(30000000);
    }

    /**
     *  这里的到的包数和wireshark得到的包数不同，因为这里是过滤掉非TCP的包的
     * @throws InterruptedException
     */
    @Test
    public void all_packet_number() throws InterruptedException {
        tsharkMainService.start(CAPTURE_COMMAND_WIN, new AbstractPacketService.ProcessCallback() {
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

    @Autowired private PcapMainService pcapMainService;

    @Test
    public void pcap_data_test() throws InterruptedException {
        DefaultPipeLine pipeLine = PacketMain.getDefaultPipeLine();
        System.out.println(pipeLine);
        System.out.println(PacketMain.pcapPipeLine);
        String ip = "192.168.1.104";
        pcapMainService.start(ip, new AbstractPacketService.ProcessCallback() {
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
        Thread.sleep(50000);
        pcapMainService.stop();
        Thread.sleep(100000000);
    }





}
