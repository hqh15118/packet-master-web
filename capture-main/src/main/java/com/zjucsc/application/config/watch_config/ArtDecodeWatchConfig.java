package com.zjucsc.application.config.watch_config;


import com.zjucsc.application.config.Common;
import com.zjucsc.art_decode.ArtDecodeUtil;
import com.zjucsc.art_decode.base.BaseConfig;
import com.zjucsc.attack.AttackCommon;
import com.zjucsc.attack.bean.ArtAttackAnalyzeConfig;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Set;

@Endpoint(id = "artdecodeconfigs")
@Configuration
public class ArtDecodeWatchConfig {

    @WriteOperation
    public Set<BaseConfig> getArtConfigsByProtocol(String protocolName){
        return ArtDecodeUtil.getArtConfig(protocolName);
    }

    @WriteOperation
    public void setDecodeDelayVisible(boolean visible){
        Common.showArtDecodeDelay = visible;
    }

    @ReadOperation
    public Map<String,Set<ArtAttackAnalyzeConfig>> getArtAttackConfigByProtocol(){
        return AttackCommon.getArtExpressionByProtocol();
    }

}
