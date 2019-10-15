package com.zjucsc.tshark.util;

public class TsharkUtil {

    private static final byte[] EMPTY = new byte[]{};
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        if (len == 0){
            return EMPTY;
        }
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

    /**
     * decode non : string
     * String trailer = "00020d04fc6aa8defba27a10fc6aa8defba27a80";
     *         String fsc = "0x00000075";
     * @param s
     * @return
     */
    public static byte[] hexStringToByteArray2(String s) {
        return hexStringToByteArray2(s,0);
    }


    public static byte[] hexStringToByteArray2(String s , int offset) {
        int len = s.length();
        if (len == 0){
            return EMPTY;
        }
        int byteArraySize = (len - offset) >>> 1;
        byte[] data = new byte[byteArraySize];
        int j = 0;
        for (int i = offset ; i < len; i += 2) {
            data[j] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
            j++;
        }
        return data;
    }
}
