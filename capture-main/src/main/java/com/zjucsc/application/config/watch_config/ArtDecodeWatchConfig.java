package com.zjucsc.application.config.watch_config;


import com.zjucsc.art_decode.ArtDecodeCommon;
import com.zjucsc.art_decode.base.BaseConfig;
import com.zjucsc.attack.AttackCommon;
import com.zjucsc.attack.bean.ArtAttackAnalyzeConfig;
import com.zjucsc.attack.s7comm.S7OptCommandConfig;
import com.zjucsc.attack.s7comm.S7OptName;
import com.zjucsc.attack.util.ArtOptAttackUtil;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Endpoint(id = "artdecodeconfigs")
@Configuration
public class ArtDecodeWatchConfig {

    @WriteOperation
    public Set<BaseConfig> getArtConfigsByProtocol(String protocolName){
        return ArtDecodeCommon.getArtConfig(protocolName);
    }

    @ReadOperation
    public Map<String,Set<ArtAttackAnalyzeConfig>> getArtAttackConfigByProtocol(){
        return AttackCommon.getArtExpressionByProtocol();
    }

}
