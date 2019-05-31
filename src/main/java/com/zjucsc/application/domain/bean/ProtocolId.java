package com.zjucsc.application.domain.bean;

import java.io.Serializable;
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
    private int id;
    private Integer protocolId;
    private String protocolName;

    public ProtocolId(){}

    public ProtocolId(Integer protocolId, String protocolName) {
        this.protocolId = protocolId;
        this.protocolName = protocolName;
    }
}
