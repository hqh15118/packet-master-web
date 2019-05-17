package com.zjucsc.application.domain.bean;


import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class OptFilterForFront {
    private int protocolId;
    private String userName;
    private String deviceId;    //deviceId是字符串
    @NotEmpty
    private List<IOptFilter> iOptFilters;

    @Data
    public static class IOptFilter{
        private int filterType;
        private int funCode;
    }
}