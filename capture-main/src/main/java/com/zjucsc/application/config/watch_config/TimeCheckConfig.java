package com.zjucsc.application.config.watch_config;


import com.zjucsc.application.util.AppCommonUtil;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;

@Endpoint(id = "timecheckconfig")
public class TimeCheckConfig {
    @WriteOperation
    public boolean timeCheck(String interfaceName){
        return AppCommonUtil.timeCheck(interfaceName);
    }
}
