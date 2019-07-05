package com.zjucsc.application.domain.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


/**
 * 
 *
 * @author hongqianhui
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Device  extends BaseResponse implements Serializable {
    private int device_id;
    private String deviceNumber;
    private int deviceType;
    private String deviceTag;
    private String deviceInfo;
    private int gPlotId;
    private String deviceIp;
    private String deviceMac;
}
