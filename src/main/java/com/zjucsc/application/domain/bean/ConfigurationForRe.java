package com.zjucsc.application.domain.bean;

import lombok.Data;

import java.util.List;

@Data
public class ConfigurationForRe {
    private int count;
    private List<ConfigurationWrapper> configurationWrappers;

    public ConfigurationForRe(){}

    public ConfigurationForRe(int count, List<ConfigurationWrapper> configurationWrappers) {
        this.count = count;
        this.configurationWrappers = configurationWrappers;
    }
}
