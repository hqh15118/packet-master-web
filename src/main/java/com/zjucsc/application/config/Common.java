package com.zjucsc.application.config;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.zjucsc.application.domain.entity.ConfigurationSetting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;


public class Common {
    public static final int SOCKET_IO_PORT = 8081;

    public static final String CAPTURE_COMMAND_WIN = "C:\\Users\\Administrator\\Desktop\\tshark_min_win\\tshark.exe -l -n -e frame.protocols -e eth.dst -e eth.src -e ip.src -e ip.addr -e tcp.srcport -e tcp.dstport -e s7comm.param.func -e modbus.func_code -T ek -c 5 -r C:\\Users\\Administrator\\Desktop\\pcap_files\\question_1531953285_02.pcap";

    public static final String CAPTURE_COMMAND_MAC = "/Applications/Wireshark.app/Contents/MacOS/tshark " +
            "-l -n -e frame.protocols -e eth.dst -e eth.src -e ip.src -e ip.addr -e tcp.srcport -e tcp.dstport -e s7comm.param.func -e modbus.func_code -e tcp.payload -e frame.cap_len" +
            " -T ek -c 5 " +
            "-r /Users/hongqianhui/JavaProjects/packet-master-web/src/main/resources/pcap/question_1531953261_01.pcap";

    /**
     * 是否已经打开某个抓包机器上的抓包服务
     */
    public static List<String> hasStartedHost = new ArrayList<>();

    /**
     * 已经接收到的报文数量
     */
    public static long recvPacketNuber = 0;
    /**
     * 已经接收到的报文流量
     */
    public static long recvPacketFlow = 0;

    /** 所有可配置的协议
     * 协议 --> 功能码 以及 对应的含义 --> 从serviceLoader中加载
     */

    public static final HashMap<String , Map<Integer,String>> CONFIGURATION_MAP = new HashMap<>();

    public static final CopyOnWriteArraySet<String> packetCollectorId = new CopyOnWriteArraySet<>();

    /** 所有要分析的协议
     *  协议 -> [功能码配置组]
     */
    public static HashMap<String,List<ConfigurationSetting.ConfigurationContent>> BAD_PACKET_FILTER = new HashMap<>();

    /**
     * BaseResponse的返回状态 - 修改起来比较方便
     */
    public static class HTTP_STATUS_CODE{
        public static final int SYS_ERROR = 500;
        public static final int NOT_FOUND = 403;
    }
}
