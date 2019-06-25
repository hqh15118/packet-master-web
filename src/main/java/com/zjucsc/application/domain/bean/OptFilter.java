package com.zjucsc.application.domain.bean;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class OptFilter extends BaseResponse implements Serializable {
    private int id;
    private String deviceNumber;
    private int funCode;
    private int protocolId;
    private int gplotId;
}
