package com.zjucsc.application.system.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.Valid;

/**
 * 
 *
 * @author hongqianhui
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("art")
@Valid
public class Art implements Serializable {
    @TableField("protocol")
    private String protocol;
    @TableField("var_name")
    private String varName;
    @TableField("offset")
    private Integer offset;
    @TableField("length")
    private Integer length;
    @TableField("dimension")
    private String dimension;
    @TableField("min")
    private String min;
    @TableField("max")
    private String max;

}
