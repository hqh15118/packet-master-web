package com.zjucsc.application.domain.bean;

import lombok.Data;

@Data
public class ArtBeanF {
    private String artName;
    private String timeStamp;
    private SimpleDevice dstDevice;
    private SimpleDevice srcDevice;
    private String protocol;
    private String funCode;
    private float value;

    @Data
    public static class SimpleDevice{
        private String deviceName;
        private String deviceMac;
        private String deviceIp;
    }
}
