package com.zjucsc.application.domain.bean;

import lombok.Data;

import java.util.List;

@Data
public class ConfigurationForFront {
    private String protocol;
    private List<ConfigurationWrapper> configurationWrappers;

    @Data
    public static class ConfigurationWrapper{
        private int fun_code;
        private String opt;

        public ConfigurationWrapper(int fun_code, String opt) {
            this.fun_code = fun_code;
            this.opt = opt;
        }
    }
}
