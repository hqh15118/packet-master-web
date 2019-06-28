package com.zjucsc.application.config;

import com.zjucsc.tshark.TsharkCommon;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@ConfigurationProperties(prefix = "tshark-config")
@Data
public class TsharkConfig {

    public TsharkConfig(){
        TsharkCommon.setErrorCallback(errorMsg -> log.error("tshark可能存在异常，请检查：{}",errorMsg));
    }

    private String modbus_filter;
    private String s7comm_filter;

}
