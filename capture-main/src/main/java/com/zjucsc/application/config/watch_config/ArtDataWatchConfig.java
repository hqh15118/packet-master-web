package com.zjucsc.application.config.watch_config;


import com.zjucsc.application.statistic.StatisticsData;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Endpoint(id = "artdatawatchconfig")
@Configuration
public class ArtDataWatchConfig {

    @ReadOperation
    public Map<String,Float> getOptCommandConfigs(){
        return StatisticsData.getGlobalArtMap();
    }
}
