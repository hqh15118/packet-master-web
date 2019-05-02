package com.zjucsc.application.config;

public class SocketIoEvent {
    //报文统计结果推送
    public static final String STATISTICS_PACKET = "statistics_packet";
    //异常操作报文推送
    public static final String BAD_OPERATION_PACKET = "bad_operation_packet";
    //异常协议报文推送
    public static final String BAD_PROTOCOL_PACKET = "bad_protocol_packet";
    //异常工艺参数推送
    public static final String BAD_ARTIFACTS = "bad_artifacts";
    //所有报文推送
    public static final String ALL_PACKET = "all_packet";
    //采集器设备运行状态
    public static final String COLLECTOR_STATE = "collector_state";
    //系统运行状态【资源使用情况】
    public static final String SYS_INFO = "sys_info";
}
