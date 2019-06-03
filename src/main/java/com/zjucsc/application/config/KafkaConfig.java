package com.zjucsc.application.config;

public class KafkaConfig {
    /*****************
     * Kafka Topics
     *****************/
    //系统正常运行日志
    public static final String SEND_NORMAL_LOG = "normal_log";
    //重要日志
    public static final String SEND_IMPORTANT_LOG = "important_log";
    //所有报文的五元组
    public static final String SEND_ALL_PACKET_FV_DIMENSION = "fv_dimension";
    //异常报文
    public static final String SEND_PACKET_EXCEPTION = "packet_exception";
    //攻击事件
    public static final String SEND_PACKET_ATTACK = "packet_attack";
}
