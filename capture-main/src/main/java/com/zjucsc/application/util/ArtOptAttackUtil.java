package com.zjucsc.application.util;

import com.zjucsc.attack.s7comm.S7OptCommandConfig;
import com.zjucsc.attack.s7comm.S7OptName;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class ArtOptAttackUtil {
    /**************************************
     * 操作名称 -- 操作具体配置
     *************************************/
    private static final ConcurrentHashMap<String, S7OptName> OPNAME_TO_OPT_CONFIG = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<String, ConcurrentSkipListSet<S7OptCommandConfig>> PROTOCOL_TO_OPTCOMMAND_CONFIG
            = new ConcurrentHashMap<>();

    /***************************************
     * @param s7OptNames
     ***************************************/
    public static void resetOpName2OptConfig(List<S7OptName> s7OptNames){
        OPNAME_TO_OPT_CONFIG.clear();
        for (S7OptName s7OptName : s7OptNames) {
            OPNAME_TO_OPT_CONFIG.put(s7OptName.getOpname(),s7OptName);
        }
    }

    public static S7OptName getS7OptNameByOpName(String opName){
        return OPNAME_TO_OPT_CONFIG.get(opName);
    }

    /****************************************
     * 协议 -- 工艺参数控制指令配置
     ***************************************/
    public static void resetProtocol2OptCommandConfig(String protocol,List<S7OptCommandConfig> s7OptCommandConfigs){
        ConcurrentSkipListSet<S7OptCommandConfig> s7OptCommandConfigSet = PROTOCOL_TO_OPTCOMMAND_CONFIG.get(protocol);
        if (s7OptCommandConfigSet == null){
            s7OptCommandConfigSet = new ConcurrentSkipListSet<>();
            PROTOCOL_TO_OPTCOMMAND_CONFIG.put(protocol,s7OptCommandConfigSet);
        }else{
            s7OptCommandConfigSet.clear();
        }
        s7OptCommandConfigSet.addAll(s7OptCommandConfigs);
    }

    public static Set<S7OptCommandConfig> getOptConfigByProtocol(String protocol){
        return PROTOCOL_TO_OPTCOMMAND_CONFIG.get(protocol);
    }
}
