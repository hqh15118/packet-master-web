package com.zjucsc.socket_io;

public class SocketIoEvent {
    //报文统计结果[流量、包数]推送
    public static final String STATISTICS_PACKET = "statistics_packet";
    //异常报文推送
    //public static final String BAD_PACKET = "bad_packet";
    //所有报文推送
    public static final String ALL_PACKET = "all_packet";
    //采集器设备运行状态变化推送
    public static final String COLLECTOR_STATE = "collector_state";
    //攻击类型推送
    public static final String ATTACK_INFO = "attack_info";
    //实时图表信息推送
    public static final String GRAPH_INFO = "graph_info";
    //系统信息
    public static final String SYS_INFO = "sys_info";
    //工艺参数
    public static final String ART_INFO = "art_info";
    //日志打印
    public static final String LOG_INFO = "log_info";
    //系统中检测到新的报文
    public static final String NEW_IP = "new_ip";
    //一定时间内最大流量报警
    public static final String MAX_FLOW_ATTACK = "max_flow";
    //攻击统计
    public static final String ATTACK_STATISTICS = "attack_statistics";
    //检测到新设备
    public static final String NEW_DEVICE = "new_device";
    //设备到设备的报文
    public static final String DEVICE_2_DEVICE_PACKETS = "d2d_packet";
    //实时五元组过滤推送
    public static final String REAL_TIME_PACKET_FILTER = "filter_packet";
    //设备流量报警
    public static final String DEVICE_FLOW_ERROR = "device_flow_error";
    //系统运行情况推送
    public static final String SYS_RUN_STATE = "sys_run_state";
    //系统运行异常
    public static final String SYS_RUN_ERROR = "sys_run_error";
    //系统任务栈溢出异常
    public static final String TASK_QUEUE_OVER_FLOW = "task_over_flow";
    //
    public static final String COMMAND = "command";

    public static final String ART_COMMAND_ATTACK = "art_command_attack";
}
