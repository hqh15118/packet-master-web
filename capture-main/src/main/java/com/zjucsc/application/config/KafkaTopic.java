package com.zjucsc.application.config;


public class KafkaTopic {
    /*****************
     * Kafka Topics
     *****************/
    //系统正常运行日志
    public static final String SEND_NORMAL_LOG = "normal_log";
    //所有报文的五元组
    public static final String SEND_ALL_PACKET_FV_DIMENSION = "fv_dimension";
    //攻击事件
    public static final String SEND_PACKET_ATTACK = "packet_attack";
    //与工艺参数解析有关的报文
    public static final String ART_PACKET = "art_packet";
    //操作指令报文
    public static final String COMMAND_PACKET = "command_packet";
}
