package com.zjucsc.application.domain.bean;


import lombok.Data;

import java.util.List;

@Data
public class OptFilter {
    private int id;
    private String userName;
    private String deviceNumber;
    private int filterType;
    private int funCode;
    private int protocolId;
    private int gplotId;

    @Data
    public static class OptFilterForFront{
        private int protocolId;
        private String userName;
        private int deviceId;
        private List<OptFilter> optFilterList;
    }

}
