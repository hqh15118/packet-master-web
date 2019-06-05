package com.zjucsc.application.domain.bean;


import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class OptFilterForFront {
    private int protocolId;
    private String fvId;
    private List<Integer> funCodes;
    private String deviceNumber;

    @Override
    public String toString() {
        return "OptFilterForFront{" +
                "protocolId=" + protocolId +
                ", fvId='" + fvId + '\'' +
                ", funCodes=" + funCodes +
                ", deviceNumber='" + deviceNumber + '\'' +
                '}';
    }
}
