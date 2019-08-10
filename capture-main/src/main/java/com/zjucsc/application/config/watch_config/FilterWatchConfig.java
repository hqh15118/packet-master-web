package com.zjucsc.application.config.watch_config;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.util.OptFilterUtil;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Endpoint(id = "filter")
@Configuration
public class FilterWatchConfig {
    @WriteOperation
    public Map<String, String> getFilter(String deviceTag){
        Map<String, String> analyzerMap = new HashMap<>();
        analyzerMap.put("_FV_FILTER", JSON.toJSONString(Common.FV_DIMENSION_FILTER_PRO.get(deviceTag), SerializerFeature.PrettyFormat));
        analyzerMap.put("_OPT_FILTER", JSON.toJSONString(OptFilterUtil.OPERATION_FILTER_PRO.get(deviceTag),SerializerFeature.PrettyFormat));
        return analyzerMap;
    }
}
