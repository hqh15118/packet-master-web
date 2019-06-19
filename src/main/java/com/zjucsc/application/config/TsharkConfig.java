package com.zjucsc.application.config;

import com.zjucsc.tshark.TsharkCommon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class TsharkConfig {

    public TsharkConfig(){
        TsharkCommon.setErrorCallback(errorMsg -> log.error("tshark可能存在异常，请检查：{}",errorMsg));
    }
}
