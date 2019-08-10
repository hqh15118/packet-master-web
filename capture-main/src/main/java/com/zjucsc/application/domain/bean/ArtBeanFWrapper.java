package com.zjucsc.application.domain.bean;

import lombok.Data;

import java.util.List;

@Data
public class ArtBeanFWrapper {
    private int count;
    private List<ArtBeanF> artBeanFS;
    public static class ArtBeanF{
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
}
