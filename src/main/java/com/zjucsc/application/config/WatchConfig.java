package com.zjucsc.application.config;

import com.corundumstudio.socketio.SocketIOClient;
import com.zjucsc.application.socketio.SocketServiceCenter;
import com.zjucsc.application.util.AppCommonUtil;
import com.zjucsc.art_decode.ArtDecodeCommon;
import com.zjucsc.kafka.KafkaCommon;
import com.zjucsc.tshark.TsharkCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        map.put("DEVICE_TAG_TO_NAME" , Common.DEVICE_TAG_TO_NAME);
        map.put("GPLOT_ID" , Common.GPLOT_ID);
        map.put("SHOW_GRAPH_SET",Common.SHOW_GRAPH_SET);
        map.put("KAFKA_SERVICE", KafkaCommon.getKafkaServiceState());
        map.put("TSHARK_ERROR_STREAM_THREAD_NUMBER", TsharkCommon.getErrorProcessThreadNumber());
        List<String> clientStrings = new ArrayList<>();
        for (SocketIOClient client : SocketServiceCenter.getAllClients()) {
            clientStrings.add(client.getRemoteAddress().toString());
        }
        map.put("SOCKET-IO-CLIENT", clientStrings);
        map.put("ALL_ART_CONFIG", ArtDecodeCommon.getAllArtConfigs());
        map.put("ART_DATA",AppCommonUtil.getGlobalArtMap());
        return map;
    }

}
