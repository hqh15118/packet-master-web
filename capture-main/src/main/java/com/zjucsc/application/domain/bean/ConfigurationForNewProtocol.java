package com.zjucsc.application.domain.bean;

import lombok.Data;

import java.util.List;

@Data
public class ConfigurationForNewProtocol {
    private String protocolName;
    private List<ConfigurationForFront.ConfigurationWrapper> configurationWrappers;
}
