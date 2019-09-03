package com.zjucsc.application.config.watch_config;


import com.zjucsc.attack.bean.BaseOpName;
import com.zjucsc.attack.config.S7OptCommandConfig;
import com.zjucsc.attack.util.ArtOptAttackUtil;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Endpoint(id = "artcommanddecodeconfigs")
@Configuration
public class ArtCommandDecodeConfig {

    @WriteOperation
    public ConcurrentHashMap<String, BaseOpName> getOptNames(){
        return ArtOptAttackUtil.OPNAME_TO_OPT_CONFIG;
    }

    @ReadOperation
    public Map<String,S7OptCommandConfig> getOptCommandConfigs(){
        return ArtOptAttackUtil.OPNAME_TO_OPT_COMMAND_CONFIG;
    }
}
