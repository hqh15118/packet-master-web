package com.zjucsc.application.domain.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 
 *
 * @author hongqianhui
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Protocol extends BaseResponse implements Serializable {
    private int id;
    private int protocolId;
    private String protocolName;

    public Protocol(){}

    public Protocol(int protocolId, String protocolName) {
        this.protocolId = protocolId;
        this.protocolName = protocolName;
    }
}
