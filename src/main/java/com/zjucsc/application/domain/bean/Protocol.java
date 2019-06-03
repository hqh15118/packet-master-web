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
public class Protocol implements Serializable {
    private int id;
    private int protocolId;
    private String protocolName;

    public Protocol(){}

    public Protocol(int protocolId, String protocolName) {
        this.protocolId = protocolId;
        this.protocolName = protocolName;
    }
}
