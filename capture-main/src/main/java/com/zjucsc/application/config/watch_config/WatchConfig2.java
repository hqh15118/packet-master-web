package com.zjucsc.application.config.watch_config;

import com.zjucsc.application.util.CommonCacheUtil;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Endpoint(id = "white-packets")
@Configuration
public class WatchConfig2 {

    @ReadOperation
    public Map<String,Object> getAllWhitePackets(){
        HashMap<String,Object> map = new HashMap<>();
        map.put("ALL_WHITE_PACKETS", CommonCacheUtil.getNormalPacketInfo());
        return map;
    }

}
