package com.zjucsc.application.domain.bean;


import lombok.Data;

@Data
public class ConfigurationWrapper {
    private String funCode;
    private String opt;

    public ConfigurationWrapper(){}

    public ConfigurationWrapper(String fun_code, String opt) {
        this.funCode = fun_code;
        this.opt = opt;
    }
}
