package com.zjucsc.application.domain.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AttackInfo {
    private int id;
    private String time;
    private String type;
    private String device_ip;
    private String src_ip;
    private String dst_ip;
    private String src_mac;
    private String dst_mac;
    private String port;
    private String dst_port;
    private String protocol;
    private String attack_info;
}
