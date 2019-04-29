package com.zjucsc.application.util;

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
}
