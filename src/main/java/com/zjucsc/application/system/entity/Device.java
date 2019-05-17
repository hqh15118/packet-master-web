package com.zjucsc.application.system.entity;

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
@TableName("device_info")
public class Device implements Serializable {
    @TableId(value = "device_id" , type = IdType.AUTO)
    private int device_id;
    @TableField("device_number")
    private String deviceNumber;
    @TableField("device_type")
    private int deviceType;
    @NotBlank
    @TableField("device_tag")
    private String deviceTag;
    @TableField("device_info")
    @NotBlank
    private String deviceInfo;
    @TableField("gplot_id")
    private int gPlotId;
}
