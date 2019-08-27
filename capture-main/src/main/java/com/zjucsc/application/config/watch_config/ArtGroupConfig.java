package com.zjucsc.application.config.watch_config;

import com.zjucsc.application.util.CacheUtil;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

@Endpoint(id = "artgroup")
@Configuration
public class ArtGroupConfig {

    @ReadOperation
    public ConcurrentHashMap<String,String> getArtName2Group(){
        return CacheUtil.ART_NAME_TO_GROUP;
    }

    @WriteOperation
    public String getArtGroupByArtName(String artName){
        return CacheUtil.getArtGroupByArtName(artName);
    }
}
