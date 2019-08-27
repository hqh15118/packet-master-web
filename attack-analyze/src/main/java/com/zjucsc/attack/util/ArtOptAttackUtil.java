package com.zjucsc.attack.util;

import com.zjucsc.attack.s7comm.CommandWrapper;
import com.zjucsc.attack.s7comm.S7OptCommandConfig;
import com.zjucsc.attack.s7comm.S7OptName;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class ArtOptAttackUtil {
    /**************************************
     * 操作名称 -- 操作具体配置
     *************************************/
    public static final ConcurrentHashMap<String, S7OptName> OPNAME_TO_OPT_CONFIG = new ConcurrentHashMap<>();
    /***************************************
     * 协议 -- 该协议下的所有操作配置
     **************************************/
    public static final ConcurrentHashMap<String,ConcurrentSkipListSet<S7OptName>> PROTOCOL_TO_OPT_CONFIGS =
            new ConcurrentHashMap<>();
    /**
     * opName -- 该opName对应的配置
     */
    public static final ConcurrentHashMap<String, S7OptCommandConfig> OPNAME_TO_OPT_COMMAND_CONFIG
            = new ConcurrentHashMap<>();

    /***************************************
     * @param s7OptNames
     ***************************************/
    public static void resetOpName2OptConfig(List<S7OptName> s7OptNames){
        OPNAME_TO_OPT_CONFIG.clear();
        for (S7OptName s7OptName : s7OptNames) {
            OPNAME_TO_OPT_CONFIG.put(s7OptName.getOpName(),s7OptName);
            Set<S7OptName> s7OptNameSet = PROTOCOL_TO_OPT_CONFIGS.putIfAbsent(s7OptName.getProtocol(),
                    new ConcurrentSkipListSet<S7OptName>(){{add(s7OptName);}});
            if (s7OptNameSet != null){
                s7OptNameSet.add(s7OptName);
            }
        }
    }

    public static S7OptName getS7OptNameByOpName(String opName){
        return OPNAME_TO_OPT_CONFIG.get(opName);
    }

    public static Set<S7OptName> getS7OptNameSetByProtocol(String protocol){
        return PROTOCOL_TO_OPT_CONFIGS.get(protocol);
    }

    /****************************************
     * 协议 -- 工艺参数控制指令配置
     ***************************************/
    public static void resetProtocol2OptCommandConfig(List<S7OptCommandConfig> s7OptCommandConfigs){
        OPNAME_TO_OPT_COMMAND_CONFIG.clear();
        for (S7OptCommandConfig s7OptCommandConfig : s7OptCommandConfigs) {
            List<String> ruleString = new ArrayList<>();
            for (CommandWrapper commandWrapper : s7OptCommandConfig.getRule()) {
                ruleString.add(commandWrapper.getValue());
            }
            s7OptCommandConfig.setRuleString(ruleString);
            OPNAME_TO_OPT_COMMAND_CONFIG.put(s7OptCommandConfig.getProcess_operate(),s7OptCommandConfig);
        }
    }

    public static S7OptCommandConfig getOptConfigByOpName(String opName){
        return OPNAME_TO_OPT_COMMAND_CONFIG.get(opName);
    }
}
