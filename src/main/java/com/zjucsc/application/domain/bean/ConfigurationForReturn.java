package com.zjucsc.application.domain.bean;

import lombok.Data;

import java.util.List;

@Data
public class ConfigurationForReturn {
    private int count;
    private List<ConfigurationWrapper> configurationWrappers;

    public ConfigurationForReturn(){}

    public ConfigurationForReturn(int count, List<ConfigurationWrapper> configurationWrappers) {
        this.count = count;
        this.configurationWrappers = configurationWrappers;
    }
}
