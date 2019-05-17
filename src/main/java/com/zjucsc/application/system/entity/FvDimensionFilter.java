package com.zjucsc.application.system.entity;

import com.alibaba.fastjson.annotation.JSONField;
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
    private String deviceNumber;
    @TableField("user_name")
    private String userName;
    @TableField("filter_type")
    private int filterType;
    @TableField("src_ip")
    private String srcIp;
    @TableField("dst_ip")
    private String dstIp;
    @TableField("src_mac")
    private String srcMac;
    @TableField("dst_mac")
    private String dstMac;
    @TableField("src_port")
    private String srcPort;
    @TableField("dst_port")
    private String dstPort;
    @TableField("protocol_id")
    private int protocolId;
    @TableField("gplot_id")
    private int gplotId;
}
