package com.zjucsc.application.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class ArtConfig{

    @TableId(value = "id" , type = IdType.AUTO)
    private int artConfigId;

    private String tag;

    private Integer offset;

    private Integer length;

    private String meaning;

    @TableField("protocol_id")
    private int protocolId;
    @TableField("min_length")
    private int minLength;

    private String paramType;

    private String srcIp;
    private String dstIp;
    @TableField("fun_code")
    private String funCode;
}
