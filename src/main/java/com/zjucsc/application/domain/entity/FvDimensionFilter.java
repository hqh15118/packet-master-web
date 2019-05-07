package com.zjucsc.application.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@TableName("fv_dimension_filter")
public class FvDimensionFilter {
    @TableId(value = "id" , type = IdType.AUTO)
    private int id;
    @TableField("device_id")
    @NotBlank
    private int deviceId;
    @TableField("user_name")
    private String user_name;
    @TableField("filter_type")
    private int filter_type;
    @TableField("src_ip")
    private String src_ip;
    @TableField("dst_ip")
    private String dst_ip;
    @TableField("src_mac")
    private String src_mac;
    @TableField("dst_mac")
    private String dst_mac;
    @TableField("src_port")
    private String src_port;
    @TableField("dst_port")
    private String dst_port;
    @TableField("protocol")
    private int protocol_id;
}