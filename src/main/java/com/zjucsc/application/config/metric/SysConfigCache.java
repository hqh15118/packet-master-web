package com.zjucsc.application.config.metric;


import com.zjucsc.application.config.Common;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Endpoint(id = "sysconfig")
public class SysConfigCache {
    @ReadOperation
    public Map<String,Object> getSysConfigCache(){
        HashMap<String,Object> map = new HashMap<>();
        map.put("CONFIGURATION_MAP" , Common.CONFIGURATION_MAP);
        map.put("PROTOCOL_STR_TO_INT", Common.PROTOCOL_STR_TO_INT);
        map.put("OPERATION_FILTER_PRO" , Common.OPERATION_FILTER_PRO);
        map.put("FV_DIMENSION_FILTER_PRO" , Common.FV_DIMENSION_FILTER_PRO);
        return map;
    }
}
