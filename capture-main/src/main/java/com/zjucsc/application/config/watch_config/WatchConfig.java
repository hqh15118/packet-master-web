package com.zjucsc.application.config.watch_config;

import com.corundumstudio.socketio.SocketIOClient;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.config.TsharkConfig;
import com.zjucsc.application.util.AppCommonUtil;
import com.zjucsc.application.util.CommonCacheUtil;
import com.zjucsc.application.util.CommonConfigUtil;
import com.zjucsc.application.util.CommonOptFilterUtil;
import com.zjucsc.art_decode.ArtDecodeCommon;
import com.zjucsc.kafka.KafkaCommon;
import com.zjucsc.socket_io.SocketServiceCenter;
import com.zjucsc.tshark.TsharkCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Endpoint(id = "sysconfig")
@Configuration
public class WatchConfig {

    @Autowired private TsharkConfig tsharkConfig;

    @ReadOperation
    public Map<String,Object> getSysConfigCache(){
        HashMap<String,Object> map = new HashMap<>();
        map.put("CONFIGURATION_MAP" , CommonCacheUtil.CONFIGURATION_MAP);
        map.put("PROTOCOL_STR_TO_INT", CommonCacheUtil.PROTOCOL_STR_TO_INT);
        map.put("OPERATION_FILTER_PRO" , CommonOptFilterUtil.OPERATION_FILTER_PRO);
        map.put("FV_DIMENSION_FILTER_PRO" , Common.FV_DIMENSION_FILTER_PRO);
        map.put("DEVICE_TAG_TO_NAME" , CommonCacheUtil.DEVICE_NUMBER_TO_TAG);
        map.put("GPLOT_ID" , Common.GPLOT_ID);
        map.put("SHOW_GRAPH_SET",CommonCacheUtil.SHOW_GRAPH_SET);
        map.put("KAFKA_SERVICE", KafkaCommon.getKafkaServiceState());
        map.put("TSHARK_ERROR_STREAM_THREAD_NUMBER", TsharkCommon.getErrorProcessThreadNumber());
        List<String> clientStrings = new ArrayList<>();
        for (SocketIOClient client : SocketServiceCenter.getAllClients()) {
            clientStrings.add(client.getRemoteAddress().toString());
        }
        map.put("SOCKET-IO-CLIENT", clientStrings);
        map.put("ART_DATA",AppCommonUtil.getGlobalArtMap());
        map.put("TSHARK-FILTER-S7COMM",tsharkConfig.getS7comm_filter());
        map.put("TSHARK-FILTER-MODBUS",tsharkConfig.getModbus_filter());
        map.put("ALL_DROP_PROTOCOL",CommonCacheUtil.getAllDropProtocol());
        return map;
    }

}
