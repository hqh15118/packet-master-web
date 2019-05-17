package com.zjucsc.application.util;

import org.junit.Test;
import org.pcap4j.core.*;
import org.pcap4j.packet.Packet;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-13 - 19:53
 */
public class PacketDecodeUtilTest {

    private String trailer = "00:03:0d:0d:fc:6b:07:e4:ae:78:63:b0:fc:6b:07:e4:ae:78:64:20";
    private String fcs = "0x00000067";

    @Test
    public void hexStringToByteArray() {
    }

    @Test
    public void hexStringToByteArray2() {
        StringBuilder sb = new StringBuilder(50);
        String trailer = "00020d04fc6aa8defba27a10fc6aa8defba27a80";
        String fsc = "0x00000075";
        sb.delete(0,50);
        sb.append(trailer);
        sb.append(fsc,2,10);
        System.out.println(sb.toString());

        sb.delete(0,50);
        sb.append(trailer);
        sb.append(fsc,2,10);
        String res = sb.toString();
        res.replace(":","");
        System.out.println(sb.toString());

        byte[] bytes = PacketDecodeUtil.hexStringToByteArray2(sb.toString());
        sb.delete(0,50);
        for (byte aByte : bytes) {
            sb.append(Integer.toHexString(Byte.toUnsignedInt(aByte))).append(":");
        }
        System.out.println(sb.toString());
    }

    @Test
    public void decodeTimeStamp() {
        String trailer = "00030d0dfc6af4ef508d6080fc6af4ef508d60c0";
        String fsc = "0x00000061";
        StringBuilder sb = new StringBuilder();
        sb.delete(0,50);
        //有些报文可能没有eth_trailer和eth_fcs
        sb.append(trailer).append(fsc,2,10);
        byte[] payload = PacketDecodeUtil.decodeTrailerAndFsc(sb.toString());
        System.out.println(PacketDecodeUtil.decodeTimeStamp(payload,20));
    }

    @Test
    public void realTimeStampDecode() throws PcapNativeException, InterruptedException, NotOpenException {
        String classPath = PacketDecodeUtilTest.class.getResource("").getPath();
        String filePath = "C:\\Users\\Administrator\\IdeaProjects\\packet-master-web\\src\\main\\resources\\pcap\\no_ether_src.pcap";
        PcapHandle handle = Pcaps.openOffline(filePath);
        handle.loop(-1, new PacketListener() {
            @Override
            public void gotPacket(Packet packet) {
                byte[] bytes = packet.getRawData();
                PacketDecodeUtil.decodeTimeStamp(bytes,20);
                System.out.println(PacketDecodeUtil.decodeCollectorDelay(bytes,4));
            }
        });
    }

    @Test
    public void decodeFuncode() {
    }

    @Test
    public void discernPacket() {
    }

    @Test
    public void decodeCollectorState() {
    }

    @Test
    public void decodeTrailerAndFsc(){
        String trailer = "00:30:d0:df:c6:af:4e:f5:08:d6:08:0f:c6:af:4e:f5:08:d6:0c:01";
        String fsc = "0x00000061";
        byte[] buffer = PacketDecodeUtil.decodeTrailerAndFsc(trailer + fsc);
        for (byte b : buffer) {
            System.out.println(Integer.toHexString(Byte.toUnsignedInt(b)));
        }
    }

    @Test
    public void decodeDelay(){
        byte[] payload = PacketDecodeUtil.decodeTrailerAndFsc(trailer + fcs);
        int delay = PacketDecodeUtil.decodeCollectorDelay(payload,4);
        System.out.println(delay);
    }
}