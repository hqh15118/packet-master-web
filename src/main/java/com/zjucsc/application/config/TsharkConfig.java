package com.zjucsc.application.config;

import com.zjucsc.tshark.TsharkCommon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class TsharkConfig {

    public TsharkConfig(){
        TsharkCommon.setErrorCallback(new TsharkCommon.ErrorCallback() {
            @Override
            public void errorCallback(String errorMsg) {
                log.error("tshark进程发生错误，错误信息：{}",errorMsg);
            }
        });
    }
}
