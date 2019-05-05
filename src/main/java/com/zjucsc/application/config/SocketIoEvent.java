package com.zjucsc.application.config;

public class SocketIoEvent {
    //报文统计结果[流量、包数]推送
    public static final String STATISTICS_PACKET = "statistics_packet";
    //异常报文推送
    public static final String BAD_PACKET = "bad_packet";
    //所有报文推送
    public static final String ALL_PACKET = "all_packet";
    //采集器设备运行状态变化推送
    public static final String COLLECTOR_STATE = "collector_state";
    //攻击类型推送
    public static final String ATTACK_INFO = "attack_info";
}
