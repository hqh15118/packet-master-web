package com.zjucsc.application.domain.bean;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;

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
}
