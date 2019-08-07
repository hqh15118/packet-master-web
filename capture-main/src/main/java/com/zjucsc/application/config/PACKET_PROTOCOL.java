package com.zjucsc.application.config;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-04 - 02:20
 *
 * @see Common Common中，协议的string和int的对应关系
 */
public interface PACKET_PROTOCOL {
    String MODBUS = "modbus";
    //@ProtocolIgnore
    String S7 = "s7comm";
//    @ProtocolIgnore
//    String S7_JOB = "s7comm_job";
//    @ProtocolIgnore
//    String S7_Ack_data = "s7comm_ack_data";
    String S7_User_data = "s7comm_user_data";
    @ProtocolIgnore
    String TCP = "tcp";
    @ProtocolIgnore
    String IPV4 = "ipv4";
    @ProtocolIgnore
    String UDP = "udp";
    @ProtocolIgnore
    String OTHER = "unknown packet";
    @ProtocolIgnore
    String ARP = "arp";
    @ProtocolIgnore
    String IPV6 = "ipv6";
    @ProtocolIgnore
    String FV_DIMENSION = "five_dimension";
    @ProtocolIgnore
    String DNS = "dns";
    @ProtocolIgnore
    String ETHERNET = "ethernet";
    String OPC_UA = "opcua";
    String IEC104_APCI = "104apci";
    String IEC104_ASDU = "104asdu";
    String DNP3_0_PRI = "dnp3.0_pri";
    String DNP3_0_SET = "dnp3.0_set";
    @ProtocolIgnore
    String NBNS = "nbns";
    @ProtocolIgnore
    String GOOSE = "goose";
    @ProtocolIgnore
    String STP = "stp";
    @ProtocolIgnore
    String CLNP = "clnp";
    @ProtocolIgnore
    String ESIS = "esis";
    @ProtocolIgnore
    String ICMPV6 = "icmpv6";
    @ProtocolIgnore
    String IGMP = "igmp";
    @ProtocolIgnore
    String LLDP = "lldp";
    @ProtocolIgnore
    String UNKNOWN = "unknown";
    String PN_IO = "pn_io";
    String CIP_IP = "cip";
    String MMS = "mms";
    String IEC101 = "iec101";
    String OPC_DA = "dcerpc";


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
    int OPC_UA_ID = 12;
    int IEC104_ASDU_ID = 13;
    int IEC104_APCI_ID = 25;
    int DNP3_0_PRI_ID = 14;//DNP3_0_PRI
    int DMP3_0_SET_ID = 26;//DNP3_0_SET   DNP3_0_SET_ID
    int NBNS_ID = 15;
    int GOOSE_ID = 16;
    int STP_ID = 17;
    int CLNP_ID = 18;
    int ESIS_ID = 19;
    int ICMPV6_ID = 20;
    int IGMP_ID = 21;
    int LLDP_ID = 22;
    int UNKNOWN_ID = -100;
    int PN_IO_ID = 23;
    int S7_User_data_ID = 24;
    int CIP_ID = 27;
    int MMS_ID = 28;
    int IEC101_ID = 29;
    int OPC_DA_ID = 30;
}
