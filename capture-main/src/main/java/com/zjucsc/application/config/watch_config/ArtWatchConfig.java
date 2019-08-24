package com.zjucsc.application.config.watch_config;


import com.zjucsc.art_decode.ArtDecodeCommon;
import com.zjucsc.art_decode.base.BaseConfig;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Endpoint(id = "artconfigs")
@Configuration
public class ArtWatchConfig {

    @WriteOperation
    public Set<BaseConfig> getArtConfigsByProtocol(String protocolName){
        return ArtDecodeCommon.getArtConfig(protocolName);
    }
}
