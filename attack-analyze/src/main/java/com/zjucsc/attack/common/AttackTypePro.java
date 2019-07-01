package com.zjucsc.attack.common;

public interface AttackTypePro {
    String DOS_ATTACK = "DOS攻击";
    String VISIT_DEVICE =  "非授权设备访问";
    String VISIT_COMMAND = "非授权指令访问";
    String VISIT_PORT = "非授权端口访问";
    String ARG_EXCEPTION = "参数异常";
    String ART_EXCEPTION = "工艺参数异常";
    String CODE_FALSIFY = "代码篡改";
    String CODE_EXCEPTION = "代码异常";
    String SNIFF_ATTACK = "嗅探攻击";
    String CONFIG_FALSIFY = "配置篡改";
    String VISIT_PROTOCOL = "非授权访问协议";
}
