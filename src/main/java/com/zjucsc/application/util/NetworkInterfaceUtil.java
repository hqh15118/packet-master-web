package com.zjucsc.application.util;

public class NetworkInterfaceUtil {
    public static String convertByteToMacAddress(byte[] macAddress){
        if (macAddress == null){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < macAddress.length; i++) {
            if (i != 0) {
                sb.append("-");
            }
            // mac[i] & 0xFF 是为了把byte转化为正整数
            String s = Integer.toHexString(macAddress[i] & 0xFF);
            // System.out.println("--------------");
            // System.err.println(s);
            sb.append(s.length() == 1 ? 0 + s : s);
        }
        // 把字符串所有小写字母改为大写成为正规的mac地址并返回
        return sb.toString().toUpperCase();
    }
}
