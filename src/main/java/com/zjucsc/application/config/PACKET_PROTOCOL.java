package com.zjucsc.application.config;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-04 - 02:20
 *
 * @see Common  看Common中，协议的string和int的对应关系
 */
public interface PACKET_PROTOCOL {
    String MODBUS = "modbus";
    String S7 = "s7comm";
    String S7_JOB = "s7comm_job";
    String S7_Ack_data = "s7comm_ack_data";
    String TCP = "tcp";
    String IPV4 = "ipv4";
    String UDP = "udp";
    String OTHER = "unknown packet";
    String ARP = "arp";
    String IPV6 = "ipv6";
    String FV_DIMENSION = "five_dimension";
    String DNS = "dns";
    String ETHERNET = "ethernet";

    int MODBUS_ID = 1;
    int S7_ID = 2;
    int S7_JOB_ID = 3;
    int S7_Ack_data_ID = 4;
    int TCP_ID = 5;
    int IPV4_ID = 6;
    int UDP_ID = 7;
    int OTHER_ID = -1;
    int FV_DIMENSION_ID = -2;
    int ARP_ID = 8;
    int IPV6_ID = 9;
    int DNS_ID = 10;
    int ETHERNET_ID = 11;
}
