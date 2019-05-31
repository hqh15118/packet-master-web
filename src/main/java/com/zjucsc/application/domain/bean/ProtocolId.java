package com.zjucsc.application.domain.bean;

import java.io.Serializable;

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
public class ProtocolId implements Serializable {
    @TableId(value = "id" , type = IdType.AUTO)
    private int id;
    @TableField("protocol_id")
    private Integer protocolId;
    @TableField("protocol_name")
    private String protocolName;

    public ProtocolId(){}

    public ProtocolId(Integer protocolId, String protocolName) {
        this.protocolId = protocolId;
        this.protocolName = protocolName;
    }
}
