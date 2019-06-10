package com.zjucsc.application.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zjucsc.kafka.KafkaCommon;
import com.zjucsc.tshark.TsharkCommon;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Endpoint(id = "sysconfig")
@Configuration
public class WatchConfig {
    @ReadOperation
    public Map<String,Object> getSysConfigCache(){
        HashMap<String,Object> map = new HashMap<>();
        map.put("CONFIGURATION_MAP" , Common.CONFIGURATION_MAP);
        map.put("PROTOCOL_STR_TO_INT", Common.PROTOCOL_STR_TO_INT);
        map.put("OPERATION_FILTER_PRO" , Common.OPERATION_FILTER_PRO);
        map.put("FV_DIMENSION_FILTER_PRO" , Common.FV_DIMENSION_FILTER_PRO);
        map.put("DEVICE_IP_TO_NAME" , Common.DEVICE_TAG_TO_NAME);
        map.put("ART_FILTER", JSON.toJSON(Common.ART_FILTER));
        map.put("GPLOT_ID" , Common.GPLOT_ID);
        map.put("SHOW_GRAPH_SET",Common.SHOW_GRAPH_SET);
        map.put("ART_DECODE_MAP",Common.ART_DECODE_MAP);
        map.put("KAFKA_SERVICE", KafkaCommon.getKafkaServiceState());
        map.put("TSHARK_ERROR_STREAM_THREAD_NUMBER", TsharkCommon.getErrorProcessThreadNumber());
        return map;
    }
}
