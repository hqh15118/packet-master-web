package com.zjucsc.application.domain.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 组态配置实体类
 */
@Data
@TableName("configurations")
public class ConfigurationSetting {
    //0 white 1 black
    @TableField("type")
    @NotBlank
    private int type;
    @NotBlank
    @TableField("ip")
    private String ip;
    @TableField("protocol")
    private String protocol;
    @TableField("port")
    private String port;
    //operation
    @TableField("fun_code")
    private String fun_code;
    //工艺参数
    @TableField("artifical_parm")
    private String artificalParm;
}
