package com.zjucsc.application.domain.bean;

import lombok.Data;

@Data
public class DeviceForScheduleSend {
    private String deviceMac;
    private String deviceName;
    private String deviceIp;
    private String deviceNumber;
}
