package com.zjucsc.application.util;

import com.zjucsc.tshark.packets.FvDimensionLayer;

public class DeviceOptUtil {

    public static String getSrcDeviceTag(FvDimensionLayer layer){
        return layer.ip_src[0].equals("--") ? layer.eth_src[0] : layer.ip_src[0];
    }

    public static String getDstDeviceTag(FvDimensionLayer layer){
        return layer.ip_dst[0].equals("--") ? layer.eth_dst[0] : layer.ip_dst[0];
    }

    /**
     * 新增设备时候判断一条报文是否属于有效报文
     * @param layer
     * @return
     */
    public static boolean validPacketInfo(FvDimensionLayer layer){
        return  !layer.eth_src[0].equals("ff:ff:ff:ff:ff:ff") && //非广播mac地址
                !layer.ip_src[0].equals("0.0.0.0") &&           //非初始化IP地址
                !layer.ip_src[0].equals("255.255.255.255"); //非广播IP地址
                //&& !layer.ip_src[0].endsWith("1");                 //非网关地址
    }
}
