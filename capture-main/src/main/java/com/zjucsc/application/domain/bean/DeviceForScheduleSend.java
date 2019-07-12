package com.zjucsc.application.domain.bean;

import lombok.Data;

@Data
public class DeviceForScheduleSend {
    private String deviceMac;
    private String deviceName;
    private String deviceIp;
    private String deviceNumber;

    @Override
    public String toString() {
        return " deviceMac='" + deviceMac + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", deviceIp='" + deviceIp + '\'' +
                ", deviceNumber='" + deviceNumber + '\'' +
                '}';
    }
}
