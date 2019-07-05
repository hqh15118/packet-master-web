package com.zjucsc;

import com.zjucsc.application.config.Common;
import com.zjucsc.application.controller.PacketController;
import com.zjucsc.application.domain.bean.RightPacketInfo;
import com.zjucsc.application.util.CommonCacheUtil;
import com.zjucsc.application.util.PacketDecodeUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


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

    @Test
    public void rightWhitePacketTest(){
        RightPacketInfo rightPacketInfo = new RightPacketInfo();
        rightPacketInfo.setDst_ip("dst_ip");
        rightPacketInfo.setDst_mac("dst_mac");
        rightPacketInfo.setSrc_ip("src_ip");
        rightPacketInfo.setSrc_mac("src_mac");
        rightPacketInfo.setProtocol("protocol");
        CommonCacheUtil.addNormalRightPacketInfo(rightPacketInfo);
        RightPacketInfo rightPacketInfo1 = new RightPacketInfo();
        rightPacketInfo1.setDst_ip("dst_ip");
        rightPacketInfo1.setDst_mac("dst_mac");
        rightPacketInfo1.setSrc_ip("src_ip");
        rightPacketInfo1.setSrc_mac("src_mac");
        rightPacketInfo1.setProtocol("protocol");
        System.out.println(CommonCacheUtil.isNormalRightPacket(rightPacketInfo1));
        rightPacketInfo1.setProtocol("protocol1");
        System.out.println(CommonCacheUtil.isNormalRightPacket(rightPacketInfo1));
        rightPacketInfo1.setProtocol("protocol");
        System.out.println(CommonCacheUtil.isNormalRightPacket(rightPacketInfo1));
        rightPacketInfo.setFunCode(1);
        System.out.println(CommonCacheUtil.isNormalRightPacket(rightPacketInfo1));
    }
}