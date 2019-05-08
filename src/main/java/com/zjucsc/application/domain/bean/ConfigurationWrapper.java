package com.zjucsc.application.domain.bean;


import lombok.Data;

@Data
public class ConfigurationWrapper {
    private int fun_code;
    private String opt;

    public ConfigurationWrapper(){}

    public ConfigurationWrapper(int fun_code, String opt) {
        this.fun_code = fun_code;
        this.opt = opt;
    }
}
