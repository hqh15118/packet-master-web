package com.zjucsc.application.util;

import com.zjucsc.application.domain.bean.Device;
import com.zjucsc.attack.AttackCommon;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.HashMap;
import java.util.regex.Pattern;

public class DeviceOptUtil {

    private static final Pattern pattern = Pattern.compile("^2(?:2[4-9]|3\\d)(?:\\.(?:25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]\\d?|0)){3}$");

    public static String getSrcDeviceTag(FvDimensionLayer layer){
        return layer.ip_src[0].equals("--") ? layer.eth_src[0] : layer.ip_src[0];
    }

    public static String getDstDeviceTag(FvDimensionLayer layer){
        return layer.ip_dst[0].equals("--") ? layer.eth_dst[0] : layer.ip_dst[0];
    }

    /**
     * 新增设备时候判断一条报文是否属于有效报文
     * @param layer
     * @return true 有效地址 false无效地址
     */
    public static boolean validPacketInfo(FvDimensionLayer layer) {
        return !layer.eth_src[0].equals("ff:ff:ff:ff:ff:ff") && //非广播mac地址
                !layer.ip_src[0].equals("0.0.0.0") &&           //非初始化IP地址
                !layer.ip_src[0].equals("255.255.255.255") && //非广播IP地址
                !pattern.matcher(layer.ip_src[0]).matches() &&    //非组播IP地址
                !multicastMacAddressOrVirtualMacAddress(layer.eth_src[0]) ;
    }

    private static final HashMap<String,String> VIRTUAL_MAC_ADDRESS =
            new HashMap<String, String>(){
                {
                    put("00:05:69","");
                    put("00:0c:29","");
                    put("00:50:56","");
                    put("00:1c:14","");
                    put("00:1c:42","");
                    put("00:03:ff","");
                    put("00:0f:4b","");
                    put("00:16:3e","");
                    put("08:00:27","");
                }
            };
    public static boolean multicastMacAddressOrVirtualMacAddress(String macAddress){
        int firstIndex = Character.digit(macAddress.charAt(1),16);
        boolean b =  (firstIndex & 0x01) == 1;
        if (b){
            return true;
        }
        return VIRTUAL_MAC_ADDRESS.containsKey(macAddress.substring(0,8));
    }

    public static void removeDeviceBindStrategy(String deviceTag){
        CommonFvFilterUtil.disableDeviceAllConfig(deviceTag);
        CommonOptFilterUtil.disableTargetDeviceAnalyzer(deviceTag);
        AttackCommon.disableDeviceDosAnalyzePoolEntry(deviceTag);
    }

    public static void removeCachedDeviceConfigs(Device device){
        CommonCacheUtil.removeDeviceNumberToTag(device.getDeviceNumber());
        CommonCacheUtil.removeDeviceNumberToName(device.getDeviceNumber());
        CommonCacheUtil.removeAllDeviceListByMacAddress(device.getDeviceTag());
        CommonCacheUtil.removeDeviceToDevicePacketPair(device);
    }
}
