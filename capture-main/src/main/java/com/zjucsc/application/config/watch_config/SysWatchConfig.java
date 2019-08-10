package com.zjucsc.application.config.watch_config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.corundumstudio.socketio.SocketIOClient;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.config.TsharkConfig;
import com.zjucsc.application.system.service.common_impl.CapturePacketServiceImpl;
import com.zjucsc.application.system.service.common_iservice.CapturePacketService;
import com.zjucsc.application.util.AppCommonUtil;
import com.zjucsc.application.util.CacheUtil;
import com.zjucsc.attack.AttackCommon;
import com.zjucsc.kafka.KafkaCommon;
import com.zjucsc.socket_io.SocketServiceCenter;
import com.zjucsc.tshark.TsharkCommon;
import com.zjucsc.tshark.pre_processor.BasePreProcessor;
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
public class SysWatchConfig {

    @Autowired private TsharkConfig tsharkConfig;
    @Autowired private CapturePacketService capturePacketService;

    @ReadOperation
    public Map<String,Object> getSysConfigCache(){
        HashMap<String,Object> map = new HashMap<>();
        map.put("CONFIGURATION_MAP" , JSON.toJSONString(CacheUtil.CONFIGURATION_MAP, SerializerFeature.PrettyFormat));
        map.put("PROTOCOL_STR_TO_INT", CacheUtil.PROTOCOL_STR_TO_INT);
        map.put("DEVICE_TAG_TO_NAME" , CacheUtil.DEVICE_NUMBER_TO_TAG);
        map.put("GPLOT_ID" , Common.GPLOT_ID);
        map.put("SHOW_GRAPH_SET", CacheUtil.SHOW_GRAPH_SET);
        map.put("KAFKA_SERVICE", KafkaCommon.getKafkaServiceState());
        //map.put("TSHARK_ERROR_STREAM_THREAD_NUMBER", TsharkCommon.getErrorProcessThreadNumber());
        List<String> clientStrings = new ArrayList<>();
        for (SocketIOClient client : SocketServiceCenter.getAllClients()) {
            clientStrings.add(client.getRemoteAddress().toString());
        }
        map.put("SOCKET-IO-CLIENT", clientStrings);
        map.put("ART_DATA",AppCommonUtil.getGlobalArtMap());
        map.put("TSHARK-FILTER-S7COMM",tsharkConfig.getS7comm_filter());
        map.put("TSHARK-FILTER-MODBUS",tsharkConfig.getModbus_filter());
        map.put("ALL_DROP_PROTOCOL", CacheUtil.getAllDropProtocol());
        map.put("MAIN_HANDLER_LOAD",capturePacketService.load());
        map.put("ATTACK_MAIN_SERVICE_SIZE", AttackCommon.getAttackMainServiceTaskSize());
        map.put("ALL_WHITE_PACKETS", CacheUtil.getNormalPacketInfo());
        List<String> allTsharkCommand = new ArrayList<>();
        for (BasePreProcessor basePreProcessor : CapturePacketServiceImpl.basePreProcessors) {
            allTsharkCommand.add(basePreProcessor.getBindCommand());
        }
        map.put("ALL_TSHARK_COMMAND",allTsharkCommand);
        return map;
    }

}
