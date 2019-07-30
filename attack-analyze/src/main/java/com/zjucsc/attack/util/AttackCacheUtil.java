package com.zjucsc.attack.util;

import java.util.concurrent.ConcurrentHashMap;

public class AttackCacheUtil {
    private final static ConcurrentHashMap<String, AttackConfigByDevice> DEVICE_ATTACK_CONFIG
             = new ConcurrentHashMap<>();
    public static boolean addOrUpdateDeviceConfig(String deviceIp,AttackConfigByDevice attackConfigByDevice){
        if (deviceIp == null){
            return false;
        }
        DEVICE_ATTACK_CONFIG.put(deviceIp,attackConfigByDevice);
        return true;
    }
    public static AttackConfigByDevice getAttackConfigByDevice(String deviceIp){
        if (deviceIp == null)
            return null;
        return DEVICE_ATTACK_CONFIG.get(deviceIp);
    }
}
