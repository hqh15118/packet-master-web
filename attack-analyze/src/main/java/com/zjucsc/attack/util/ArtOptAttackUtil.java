package com.zjucsc.attack.util;

import com.zjucsc.attack.bean.BaseOpName;
import com.zjucsc.attack.s7comm.CommandWrapper;
import com.zjucsc.attack.config.S7OptCommandConfig;
import com.zjucsc.attack.s7comm.S7OpName;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class ArtOptAttackUtil {
    /**************************************
     * 操作名称 -- 操作具体配置       开闸 -- 如何识别开闸的报文
     *************************************/
    public static final ConcurrentHashMap<String, BaseOpName> OPNAME_TO_OPT_CONFIG = new ConcurrentHashMap<>();
    /***************************************
     * 协议 -- 该协议下的所有操作配置  s7comm -- Set 所有跟s7comm相关的操作配置
     **************************************/
    public static final ConcurrentHashMap<String,ConcurrentSkipListSet<BaseOpName>> PROTOCOL_TO_OPT_CONFIGS =
            new ConcurrentHashMap<>();
    /**
     * opName -- 该opName对应的配置
     */
    public static final ConcurrentHashMap<String, S7OptCommandConfig> OPNAME_TO_OPT_COMMAND_CONFIG
            = new ConcurrentHashMap<>();

    /***************************************
     * 重置opName 配置
     * opName -- 配置（用于解析协议指令）
     ***************************************/
    public static void resetOpName2OptConfig(List<? extends BaseOpName> allBaseOpNames){
        OPNAME_TO_OPT_CONFIG.clear();
        PROTOCOL_TO_OPT_CONFIGS.clear();
        for (BaseOpName baseOpName : allBaseOpNames) {
            OPNAME_TO_OPT_CONFIG.put(baseOpName.getOpName(),baseOpName);
            Set<BaseOpName> baseOpNameSet = PROTOCOL_TO_OPT_CONFIGS.putIfAbsent(baseOpName.getProtocol(),
                    new ConcurrentSkipListSet<BaseOpName>(){{add(baseOpName);}});
            if (baseOpNameSet != null){
                baseOpNameSet.add(baseOpName);
            }
        }
    }

    public static BaseOpName getOptNameByOpName(String opName){
        return OPNAME_TO_OPT_CONFIG.get(opName);
    }

    public static Set<BaseOpName> getOptNameSetByProtocol(String protocol){
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
