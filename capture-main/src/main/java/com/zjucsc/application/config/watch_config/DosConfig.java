package com.zjucsc.application.config.watch_config;


import com.alibaba.fastjson.JSON;
import com.zjucsc.attack.AttackCommon;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.context.annotation.Configuration;

@Endpoint(id = "dosconfigs")
@Configuration
public class DosConfig {

    @ReadOperation
    public String checkDosConfig(){
        return JSON.toJSONString(AttackCommon.ANALYZE_POOL_ENTRY_CONCURRENT_HASH_MAP);
    }
}
