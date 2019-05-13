package com.zjucsc.application.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-13 - 19:53
 */
public class PacketDecodeUtilTest {

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
}