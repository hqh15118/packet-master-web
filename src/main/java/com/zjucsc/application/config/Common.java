package com.zjucsc.application.config;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.zjucsc.application.domain.bean.CollectorState;
import com.zjucsc.application.handler.ThreadExceptionHandler;
import com.zjucsc.application.tshark.analyzer.ArtAnalyzer;
import com.zjucsc.application.tshark.analyzer.FiveDimensionAnalyzer;
import com.zjucsc.application.tshark.analyzer.OperationAnalyzer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class Common {

    public static final int SOCKET_IO_PORT = 8081;

    public static final String CAPTURE_COMMAND_WIN = "C:\\Users\\Administrator\\Desktop\\tshark_min_win\\tshark.exe -l -n -Y tcp -e frame.protocols -e eth.dst -e frame.cap_len -e eth.src -e ip.src -e ip.dst -e tcp.srcport -e tcp.dstport -e s7comm.param.func -e modbus.func_code -e tcp.payload -e s7comm.header.rosctr -T ek -e eth.trailer -e eth.fcs -r C:\\Users\\Administrator\\IdeaProjects\\packet-master-web\\src\\main\\resources\\pcap\\question_1531953285_02_notime.pcap";

    public static final String CAPTURE_COMMAND_MAC = "tshark -l -n -e frame.protocols -e eth.dst -e frame.cap_len -e eth.src -e ip.src -e ip.dst -e tcp.srcport -e tcp.dstport -e s7comm.param.func -e modbus.func_code -e tcp.payload -e s7comm.header.rosctr -T ek -r /Users/hongqianhui/JavaProjects/packet-master-web/src/main/resources/pcap/question_1531953261_01.pcap";

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

    /**
     * 采集器的时延，key是采集器的ID，value是解析出来的最大时延
     */
    public static ConcurrentHashMap<Integer,Long> COLLECTOR_DELAY_MAP = new ConcurrentHashMap<>();

    /**
     * 异常数【五元组和过滤规则】
     */
    public static long exceptionCound = 0;

    /** 所有可配置的协议
     * 协议 --> 功能码 以及 对应的含义 --> 从serviceLoader中加载
     */

    public static final HashMap<String , HashMap<Integer,String>> CONFIGURATION_MAP = new HashMap<>();

    /**
     * String 设备ID
     * String -> 协议
     * OperationAnalyzer -> 报文操作分析器
     */
    public static ConcurrentHashMap<String,ConcurrentHashMap<String, OperationAnalyzer>> OPERATION_FILTER_PRO =
            new ConcurrentHashMap<>();

    /**
     * 设备ID
     */
    public static ConcurrentHashMap<String, FiveDimensionAnalyzer> FV_DIMENSION_FILTER_PRO = new ConcurrentHashMap<>();

    /**
     * 工艺分析 协议 -- 工艺参数分析器
     */
    public static ConcurrentHashMap<String , ArtAnalyzer> ART_FILTER = new ConcurrentHashMap<>();

    /**
     * BaseResponse的返回状态 - 修改起来比较方便
     */
    public static class HTTP_STATUS_CODE{
        public static final int SYS_ERROR = 500;
        public static final int NOT_FOUND = 403;
        public static final int JSON_ERROR = 1;
        public static final int DEVICE_ERROR = 201;
        public static final int PROTOCOL_ID_ERROR = 202;
        public static final int COMMAND_NOT_VALID = 203;
        public static final int SQL_ERROR = 204;
        public static final int TOKEN_NOT_VALID = 205;
    }

    //已经登录过的用户
    public final static List<String> LOGGINED_USERS = new ArrayList<>();
    //攻击的统计信息【攻击种类 - 攻击次数】
    public final static ConcurrentHashMap<String,Integer> ATTACK_TYPE_STATICS = new ConcurrentHashMap<>();

    public final static ConcurrentHashMap<Integer, CollectorState> COLLECTOR_STATE_MAP = new ConcurrentHashMap<>();


    public static final ThreadExceptionHandler COMMON_THREAD_EXCEPTION_HANDLER = new ThreadExceptionHandler();

    /**
     *  init in
     * @see com.zjucsc.application.task.InitConfigurationService
     */
    public static final BiMap<Integer,String> PROTOCOL_STR_TO_INT = HashBiMap.create();


    public static final BiMap<Integer,String> AUTH_MAP = HashBiMap.create();

    public static final Set<String> CAPTURE_PROTOCOL = new HashSet<>();
}
