package com.zjucsc.application.config.watch_config;


import com.zjucsc.application.system.service.ScheduledService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.context.annotation.Configuration;

@Endpoint(id = "tsharkprocess")
@Configuration
public class OperationConfig {

    @Autowired private ScheduledService scheduledService;

    @ReadOperation
    public void restartTsharkProcess(){
        scheduledService.restartTsharkProcess();
    }
}
