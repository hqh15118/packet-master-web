package com.zjucsc.application.domain.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ConfigurationForFront {
    private int protocolId;
    private List<ConfigurationWrapper> configurationWrappers;

    @Data
    public static class ConfigurationWrapper implements Serializable {
        private int fun_code;
        private String opt;

        public ConfigurationWrapper(){}

        public ConfigurationWrapper(int fun_code, String opt) {
            this.fun_code = fun_code;
            this.opt = opt;
        }
    }
}
