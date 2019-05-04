package com.zjucsc.application.config;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-04 - 02:20
 */
public interface PACKET_PROTOCOL {
    String MODBUS = "modbus";
    String S7 = "s7comm";
    String S7_JOB = "s7comm_job";
    String S7_Ack_data = "s7comm_ack_data";
    String TCP = "tcp";
    String IP = "ip";
    String UDP = "udp";
    String OTHER = "other";


    String FV_DIMENSION = "five_dimension";
}
