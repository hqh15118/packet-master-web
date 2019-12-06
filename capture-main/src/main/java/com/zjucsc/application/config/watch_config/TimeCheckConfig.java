package com.zjucsc.application.config.watch_config;


import com.zjucsc.application.util.AppCommonUtil;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.context.annotation.Configuration;

@Endpoint(id = "timecheckconfig")
@Configuration
public class TimeCheckConfig {
    @WriteOperation
    public boolean timeCheck(String interfaceName){
        return AppCommonUtil.timeCheck(interfaceName);
    }
}
