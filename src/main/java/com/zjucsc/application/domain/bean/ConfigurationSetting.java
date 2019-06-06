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
public class ConfigurationSetting  extends BaseResponse implements Serializable {
    private int id;
    private int protocolId;
    private int funCode;
    private String opt;
}
