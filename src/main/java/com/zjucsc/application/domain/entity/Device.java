package com.zjucsc.application.domain.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * 
 *
 * @author hongqianhui
 */
@Data
public class Device implements Serializable {

    @TableField("device_type")
    private Integer deviceType;
    @TableField("device_ip")
    private String deviceIp;
    @TableField("device_info")
    private String deviceInfo;
}
