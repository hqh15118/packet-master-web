package com.zjucsc.application.domain.bean;

import java.io.Serializable;

import lombok.Data;


/**
 * 
 *
 * @author hongqianhui
 */
@Data
public class Device implements Serializable {
    private int device_id;
    private String deviceNumber;
    private int deviceType;
    private String deviceTag;
    private String deviceInfo;
    private int gPlotId;
    private String deviceMac;
}
