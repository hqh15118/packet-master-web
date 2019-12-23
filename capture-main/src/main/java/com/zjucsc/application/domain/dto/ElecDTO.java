package com.zjucsc.application.domain.dto;

import com.zjucsc.application.validator.IPV4;
import com.zjucsc.application.validator.elec.ElecProtocol;
import lombok.Data;

import java.util.List;

@Data
public class ElecDTO {
    @ElecProtocol
    private String protocol;
    @IPV4
    private String ipAddress;
    private List<String> id;
}
