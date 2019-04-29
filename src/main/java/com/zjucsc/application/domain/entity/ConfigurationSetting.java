package com.zjucsc.application.domain.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 组态配置实体类
 */
@Data
@TableName("configurations_setting")
public class ConfigurationSetting {
    @TableId(value = "id" , type = IdType.AUTO)
    private int id;
    @TableField(value = "src_ip")
    private String src_ip;
    @TableField(value = "dst_ip")
    private String dst_ip;
    @TableField(value = "src_mac")
    private String src_mac;
    @TableField(value = "dst_mac")
    private String dst_mac;
    @TableField(value = "port")
    private String port;
    @TableField(value = "protocol")
    private String protocol;
}
