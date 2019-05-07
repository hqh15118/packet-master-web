package com.zjucsc.application.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-06 - 23:16
 */

@TableName("configuration")
@Data
public class Configuration {
    @TableId(value = "id" , type = IdType.AUTO)
    private int id;
    @TableField("protocol_id")
    private int protocolId;
    @TableField("fun_code")
    private int fun_code;
    @TableField("opt")
    private String opt;

}
