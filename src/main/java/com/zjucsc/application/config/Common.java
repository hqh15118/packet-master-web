package com.zjucsc.application.config;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.zjucsc.application.domain.bean.ArtConfig;
import com.zjucsc.application.domain.bean.CollectorState;
import com.zjucsc.application.domain.bean.StatisticInfoSaveBean;
import com.zjucsc.application.handler.ThreadExceptionHandler;
import com.zjucsc.application.tshark.analyzer.ArtAnalyzer;
import com.zjucsc.application.tshark.analyzer.FiveDimensionAnalyzer;
import com.zjucsc.application.tshark.analyzer.OperationAnalyzer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class Common {

    public static String OS_NAME = System.getProperty("os.name");

    public static int GPLOT_ID = 0;

    public static volatile String FV_DIMENSION_STR_IN_REDIS = new Date().toString();

    public static final int SOCKET_IO_PORT = 8081;

    public static volatile boolean SCHEDULE_RUNNING = false;

    /**
     * 无trailer + fcs，无法解析时间戳的报文数量
     */
    public static long NON_TIMESTAMP_PACKET_COUNT = 0;

    public static volatile long maxFlowInByte = Long.MAX_VALUE;

    /**
     * 是否已经打开某个抓包机器上的抓包服务
     */
    public static List<String> hasStartedHost = new ArrayList<>();

    public static final AtomicInteger FLOW = new AtomicInteger(0);

    /** cache7
     * 所有可配置的协议
     * 协议 --> 功能码 以及 对应的含义 --> 从serviceLoader中加载
     */
    public static final HashMap<String , HashMap<Integer,String>> CONFIGURATION_MAP = new HashMap<>();

    /** cache6
     * String 设备IP
     * String -> 协议
     * OperationAnalyzer -> 报文操作分析器
     */
    public static ConcurrentHashMap<String,ConcurrentHashMap<String, OperationAnalyzer>> OPERATION_FILTER_PRO =
            new ConcurrentHashMap<>();

    public static final ConcurrentHashMap<String, StatisticInfoSaveBean> STATISTICS_INFO_BEAN =
            new ConcurrentHashMap<>();
    /** cache5
     * DEVICE_IP 五元组过滤器
     */
    public static ConcurrentHashMap<String, FiveDimensionAnalyzer> FV_DIMENSION_FILTER_PRO = new ConcurrentHashMap<>();

    /** cache4
     * 工艺分析 协议 -- 工艺参数分析器
     */
    public static ArtAnalyzer ART_FILTER ;

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
        public static final int ART_CONFIG_NOT_VALID = 206;
        public static final int DATA_REPEAT = 207;
        public static final int SCRIPT_UP_LOAD_FAIL = 208;
        public static final int ART_DELETE_FAIL = 209;
        public static final int ATTACK_HANDLE_ERROR = 210;
    }

    //已经登录过的用户
    public final static List<String> LOGGINED_USERS = new ArrayList<>();

    //攻击的统计信息【攻击种类 - 攻击次数】
    public final static ConcurrentHashMap<String,Integer> ATTACK_TYPE_STATICS = new ConcurrentHashMap<>();

    /**
     * cache3
     */
    public final static ConcurrentHashMap<Integer, CollectorState> COLLECTOR_STATE_MAP = new ConcurrentHashMap<>();

    public static final ThreadExceptionHandler COMMON_THREAD_EXCEPTION_HANDLER = new ThreadExceptionHandler();

    /** cache2
     *  init in
     * @see com.zjucsc.application.task.InitConfigurationService
     */
    public static final BiMap<Integer,String> PROTOCOL_STR_TO_INT = HashBiMap.create();

    public static final BiMap<Integer,String> AUTH_MAP = HashBiMap.create();

    /**
     * 给tshark用的，表示设置的要捕获的协议集合
     */
    public static final Set<String> CAPTURE_PROTOCOL = new HashSet<>();

    public static final List<Process> TSHARK_RUNNING_PROCESS = new ArrayList<>();

    /** cache1
     * 设备IP和DEVICE_NUMBER之间互相转换
     */
    public static final BiMap<String,String> DEVICE_IP_TO_NAME = HashBiMap.create();

    /**
     * 要显示的工艺参数集合【将这个set里面的工艺参数数据传输到前端，其他的不用传】
     */
    public static final Set<String> SHOW_GRAPH_SET = Collections.synchronizedSet(new HashSet<>());

    /**
     * 【协议 --> 【ArtConfigId，ArtConfigInstance】】
     */
    public static final ConcurrentHashMap<String,ConcurrentHashMap<Integer, ArtConfig>> ART_DECODE_MAP
            = new ConcurrentHashMap<>();

}
