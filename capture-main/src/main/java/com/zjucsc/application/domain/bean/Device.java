package com.zjucsc.application.domain.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 
 *
 * @author hongqianhui
 */
@Data
public class Device extends BaseResponse implements Serializable {
    private int device_id;
    private String deviceNumber;
    private int deviceType;
    private String deviceTag;
    private String deviceInfo;
    private int gPlotId;
    private String deviceIp;
    private String deviceMac;
    private boolean isConfig;
    private String protocol;
    private String createTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Device)) return false;
        Device device = (Device) o;
        return Objects.equals(getDeviceMac(), device.getDeviceMac());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDeviceMac());
    }

    @Override
    public String toString() {
        return "Device{" +
                "deviceNumber='" + deviceNumber + '\'' +
                ", deviceType=" + deviceType +
                ", deviceTag='" + deviceTag + '\'' +
                ", deviceInfo='" + deviceInfo + '\'' +
                ", gPlotId=" + gPlotId +
                ", deviceIp='" + deviceIp + '\'' +
                ", deviceMac='" + deviceMac + '\'' +
                '}';
    }
}
