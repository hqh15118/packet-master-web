package com.zjucsc.application.config.watch_config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
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
    public Map<String,Object> getConfigs2(){
        HashMap<String,Object> map = new HashMap<>();
        map.put("ALL_WHITE_PACKETS", CommonCacheUtil.getNormalPacketInfo());
        map.put("DEVICE_2_DEVICE_PACKETS", JSON.toJSONString(CommonCacheUtil.getDeviceToDevicePackets(), SerializerFeature.PrettyFormat));
        return map;
    }

}
