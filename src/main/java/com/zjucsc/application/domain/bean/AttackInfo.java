package com.zjucsc.application.domain.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@TableName("attack_info")
public class AttackInfo {
    @TableId(value = "id" , type = IdType.AUTO)
    private int id;
    @NotBlank
    @TableField("time")
    private String time;
    @NotBlank
    @TableField("type")
    private String type;
    @NotBlank
    @TableField("device_ip")
    private String device_ip;
    @TableField("src_ip")
    private String src_ip;
    @TableField("dst_ip")
    private String dst_ip;
    @TableField("src_mac")
    private String src_mac;
    @TableField("dst_mac")
    private String dst_mac;
    @TableField("src_port")
    private String port;
    @TableField("dst_port")
    private String dst_port;
    @TableField("dst_port")
    private String protocol;
    @TableField("attack_info")
    private String attack_info;
}
