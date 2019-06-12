package com.zjucsc.application.domain.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class BaseArtConfig extends BaseResponse {
    private int protocolId;
    private Object configStructure;
}
