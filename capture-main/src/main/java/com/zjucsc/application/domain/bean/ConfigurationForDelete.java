package com.zjucsc.application.domain.bean;

import lombok.Data;

import java.util.List;

@Data
public class ConfigurationForDelete {
    private List<String> funCodes;
    private int protocolId;
}
