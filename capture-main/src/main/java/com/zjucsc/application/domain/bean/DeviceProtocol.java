package com.zjucsc.application.domain.bean;


import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class DeviceProtocol extends BaseResponse{
    private int deviceId;
    private String deviceNumber;
    private String[] protocolName;
}
