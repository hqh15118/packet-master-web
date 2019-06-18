package com.zjucsc.application;

import com.alibaba.fastjson.JSON;
import com.zjucsc.application.config.StatisticsData;
import com.zjucsc.application.domain.bean.CaptureService;
import com.zjucsc.application.domain.bean.FvDimensionWrapper;
import com.zjucsc.application.controller.PacketController;
import com.zjucsc.application.system.service.common_impl.CapturePacketServiceImpl;
import com.zjucsc.application.system.service.common_impl.NetworkInterfaceServiceImpl;
import com.zjucsc.application.tshark.capture.ProcessCallback;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import com.zjucsc.tshark.pre_processor.BasePreProcessor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.SocketException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-13 - 20:34
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class CaptureNetworkInterfaceServiceImplTest {

    private String macAddressForWin = "28:D2:44:5F:69:E1";
    private String macAddressForMac = "8c:85:90:93:15:a2";

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

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
        CaptureService captureService = new CaptureService();
        captureService.setMacAddress(macAddressForWin);
        captureService.setService_ip(ipForWin);
        captureService.setService_name("en0");
        packetController.startCaptureService(captureService);
        Thread.sleep(1000000);
    }

    /**
     * 该测试中
     * 1. 报文推送效率：1秒钟大概是100万条；
     * 2. 报文消费效率：1秒钟大概是150万条；
     * 5113     500万条推送时延
     * 4940653
     * 4699841
     * 4362494
     * 2901736  JIT优化
     * 1438867
     * @throws InterruptedException
     */
    @Test
    public void testAnalyzeSpeed() throws InterruptedException {
        FvDimensionLayer fvDimensionLayer = new FvDimensionLayer();
        fvDimensionLayer.frame_protocols = new String[]{"s7comm"};
        fvDimensionLayer.ip_dst = new String[]{"192.168.0.121"};
        fvDimensionLayer.dst_port = new String[]{"9090"};
        fvDimensionLayer.src_port = new String[]{"8989"};
        fvDimensionLayer.eth_dst = new String[]{"11:22:22:33:44:55"};
        fvDimensionLayer.eth_src = new String[]{"11:22:22:33:44:66"};
        fvDimensionLayer.ip_src = new String[]{"192.168.0.122"};
        fvDimensionLayer.frame_cap_len = new String[]{"10"};

        String trailer = "00:03:0d:0d:fc:6b:07:e4:ae:78:63:b0:fc:6b:07:e4:ae:78:64:20";
        CapturePacketServiceImpl capturePacketService = new CapturePacketServiceImpl();
        long time1 = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            capturePacketService.fvDimensionLayerAbstractAsyncHandler.handleAndPass(fvDimensionLayer);
        }
        System.out.println(System.currentTimeMillis() - time1);
        System.out.println((((ThreadPoolExecutor) capturePacketService.fvDimensionLayerAbstractAsyncHandler.getExecutor())).getQueue().size()
        );
        Thread.sleep(1000);
        System.out.println((((ThreadPoolExecutor) capturePacketService.fvDimensionLayerAbstractAsyncHandler.getExecutor())).getQueue().size()
        );
        Thread.sleep(1000);
        System.out.println((((ThreadPoolExecutor) capturePacketService.fvDimensionLayerAbstractAsyncHandler.getExecutor())).getQueue().size()
        );
        Thread.sleep(1000);
        System.out.println((((ThreadPoolExecutor) capturePacketService.fvDimensionLayerAbstractAsyncHandler.getExecutor())).getQueue().size()
        );
        Thread.sleep(1000);
        System.out.println((((ThreadPoolExecutor) capturePacketService.fvDimensionLayerAbstractAsyncHandler.getExecutor())).getQueue().size()
        );
        System.out.println(StatisticsData.getAllIpCollections());
        System.out.println(StatisticsData.getProtocolNum());
    }

    @Test
    public void fvDimensionJSON(){
        FvDimensionLayer fvDimensionLayer = new FvDimensionLayer();
        fvDimensionLayer.frame_protocols = new String[]{"s7comm"};
        fvDimensionLayer.ip_dst = new String[]{"192.168.0.121"};
        fvDimensionLayer.dst_port = new String[]{"9090"};
        fvDimensionLayer.src_port = new String[]{"8989"};
        fvDimensionLayer.eth_dst = new String[]{"11:22:22:33:44:55"};
        fvDimensionLayer.eth_src = new String[]{"11:22:22:33:44:66"};
        fvDimensionLayer.ip_src = new String[]{"192.168.0.122"};
        fvDimensionLayer.frame_cap_len = new String[]{"10"};
        fvDimensionLayer.timeStamp = "2019-06-02:xxxx";
        String trailer = "00:03:0d:0d:fc:6b:07:e4:ae:78:63:b0:fc:6b:07:e4:ae:78:64:20";
        System.out.println(JSON.toJSONString(FvDimensionWrapper.builder()
                .collectorId(3)
                .delay(100)
                .layer(fvDimensionLayer)
                .build()));
    }

    @Test
    public void getAllNetworkInterface() {
    }

    @Test
    public void getAllNetworkInterfaceFlush() throws SocketException {
        NetworkInterfaceServiceImpl networkInterfaceService = new NetworkInterfaceServiceImpl();
        System.out.println(networkInterfaceService.getAllNetworkInterface());
    }
}