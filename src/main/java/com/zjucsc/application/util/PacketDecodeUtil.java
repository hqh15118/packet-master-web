package com.zjucsc.application.util;

import com.zjucsc.application.tshark.domain.beans.TimeStamp;

import static com.zjucsc.application.tshark.domain.beans.PacketInfo.PACKET_PROTOCOL.MODBUS;
import static com.zjucsc.application.tshark.domain.beans.PacketInfo.PACKET_PROTOCOL.S7;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-04-29 - 21:19
 */
public class PacketDecodeUtil {

    /*
     *  string : 03:00:00:1f:02:f0:80:32:01:00:00:cc:c1:00:0e:00:00:04:01:12:0a:10:02:00:11:00:01:84:00:00:20
     *  ----> byte[] : 03 00 00 1f 02 f0 80 32 01 00 00 cc c1 00 0e 00 00 04 01 12 0a 10 02 00 11 00 01 84 00 00 20
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        int byteArraySize = len / 3;
        byte[] data = new byte[byteArraySize + 1];
        for (int i = 0 ; i < len; i+=3) {
            data[i / 3] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        data[byteArraySize ] = (byte) ((Character.digit(s.charAt(len - 2), 16) << 4)
                + Character.digit(s.charAt(len - 1), 16));
        return data;
    }


    private static ThreadLocal<StringBuilder> stringBuilderThreadLocal
            = ThreadLocal.withInitial(() -> new StringBuilder(100));
    /**
     *
     * @param payload tcp payload
     * @param offset offset from payload end
     * @return
     */
    public static String decodeTimeStamp(byte[] payload , int offset){
        if (offset == 20) {
            return "not support now";
        }
        StringBuilder sb = stringBuilderThreadLocal.get();
        sb.delete(0,sb.length());
        int len = payload.length;
        offset = len - offset;
        int year = Byte.toUnsignedInt(payload[offset]) >>> 3;
        int var1 =  (payload[offset] & 0b00000111) << 6;
        int var2 = (payload[offset + 1] & 0b11111100) >>> 2;
        int day = var1 + var2;
        var1 = ((payload[offset + 1] & 0b00000011)) << 3;
        var2 = ((payload[offset + 2] & 0b11100000)) >>> 5;
        int hour = var1 + var2;
        var1 = ((payload[offset + 2] & 0b00011111)) << 1;
        var2 = ((payload[offset + 3] & 0b10000000)) >>> 7;
        int minute =  var1 + var2;
        int second = ((payload[offset + 3] & 0b01111110)) >>> 1 ;
        var1 = ((payload[offset + 3] & 0b00000001)) << 10;
        var2 = (payload[offset + 4]) << 1;
        int var3 = ((payload[offset + 5] & 0b10000000)) >>> 7;
        int millsecond =  var1 + var2 + var3;
        var1 = ((payload[offset + 5] & 0b01111111)) << 3;
        var2 = ((payload[offset + 6] & 0b11100000)) >>> 5;
        int unsecond = var1 + var2;
        var1 = (payload[offset + 6] & 0b00011100) >>> 2;
        int nansecond = var1 * 200;
        return sb.append(year).append(" 年 ")
                .append(day).append(" 天 ")
                .append(hour).append(" 时 ")
                .append(minute).append(" 分 ")
                .append(second).append(" 秒 ")
                .append(millsecond).append( " 毫秒 ")
                .append(unsecond).append(" 微秒 ")
                .append(nansecond).append(" 纳秒 ").toString();
    }

    public static int decodeFuncode(String str_fun_code , String protocol){
        switch (protocol){
            case MODBUS :

                break;
            case S7:

                break;
        }
        return 0;
    }

}
