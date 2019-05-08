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
    @TableId(value = "id" , type = IdType.NONE)
    private String id;
    @NotBlank
    @TableField("device_type")
    private int deviceType;
    @NotBlank
    @TableField("device_ip")
    private String deviceIp;
    @TableField("device_info")
    @NotBlank
    private String deviceInfo;
}
