package com.zjucsc.application.system.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 
 *
 * @author hongqianhui
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("configuration_setting")
public class ConfigurationSetting implements Serializable {
    @TableId(value = "id" , type = IdType.AUTO)
    private int id;
    @TableField(value = "protocol_id")
    private int protocolId;
    @TableField("fun_code")
    private int funCode;
    @TableField("opt")
    private String opt;
}
