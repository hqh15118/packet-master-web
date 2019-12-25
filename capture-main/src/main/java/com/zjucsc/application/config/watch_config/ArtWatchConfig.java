package com.zjucsc.application.config.watch_config;


import com.alibaba.fastjson.JSON;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.statistic.StatisticsData;
import com.zjucsc.application.util.CacheUtil;
import com.zjucsc.art_decode.ArtDecodeUtil;
import com.zjucsc.art_decode.base.BaseConfig;
import com.zjucsc.attack.AttackCommon;
import com.zjucsc.attack.bean.ArtAttackAnalyzeConfig;
import com.zjucsc.attack.bean.BaseOpName;
import com.zjucsc.attack.config.S7OptCommandConfig;
import com.zjucsc.attack.util.ArtOptAttackUtil;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Endpoint(id = "artwatchconfig")
@Configuration
public class ArtWatchConfig {

//    @WriteOperation
//    public ConcurrentHashMap<String, BaseOpName> getOptName2OpConfig(){
//        return ArtOptAttackUtil.OPNAME_TO_OPT_CONFIG;
//    }
//
//    @ReadOperation
//    public Map<String,S7OptCommandConfig> getOptName2OptCommandConfig(){
//        return ArtOptAttackUtil.OPNAME_TO_OPT_COMMAND_CONFIG;
//    }

    @WriteOperation
    public String getAllArtDataAndShowSet(){
        StringBuilder sb = new StringBuilder();
        return sb.append(JSON.toJSONString(StatisticsData.getGlobalArtMap())).append("\n")
                .append(CacheUtil.SHOW_GRAPH_SET).toString();
    }

    /*
    @WriteOperation
    public Set<BaseConfig> getArtConfigsByProtocol(String protocolName){
        return ArtDecodeUtil.getArtConfig(protocolName);
    }

    @WriteOperation
    public void setDecodeDelayVisible(boolean visible){
        Common.showArtDecodeDelay = visible;
    }

    @WriteOperation
    public Map<String,Set<ArtAttackAnalyzeConfig>> getArtAttackConfigByProtocol(String arg2,String arg1,String arg3){
        return AttackCommon.getArtExpressionByProtocol();
    }

    @WriteOperation
    public ConcurrentHashMap<String,String> getArtName2Group(String value){
        return CacheUtil.ART_NAME_TO_GROUP;
    }

    @WriteOperation
    public String getArtGroupByArtName(String artName){
        return CacheUtil.getArtGroupByArtName(artName);
    }

    */

}
