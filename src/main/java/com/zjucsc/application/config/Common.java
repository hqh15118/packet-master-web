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

    public static String OS_NAME = System.getProperty("os.name");

    public static int GPLOT_ID = -1;

    public static final int SOCKET_IO_PORT = 8081;

    /**
     * 是否已经打开某个抓包机器上的抓包服务
     */
    public static List<String> hasStartedHost = new ArrayList<>();

    /** cache7
     * 所有可配置的协议
     * 协议 --> 功能码 以及 对应的含义 --> 从serviceLoader中加载
     */
    public static final HashMap<String , HashMap<Integer,String>> CONFIGURATION_MAP = new HashMap<>();

    /** cache6
     * String DEVICE_NUMBER
     * String -> 协议
     * OperationAnalyzer -> 报文操作分析器
     */
    public static ConcurrentHashMap<String,ConcurrentHashMap<String, OperationAnalyzer>> OPERATION_FILTER_PRO =
            new ConcurrentHashMap<>();

    /** cache5
     * DEVICE_NUMBER 五元组过滤器
     */
    public static ConcurrentHashMap<String, FiveDimensionAnalyzer> FV_DIMENSION_FILTER_PRO = new ConcurrentHashMap<>();

    /** cache4
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


    public static final Set<String> CAPTURE_PROTOCOL = new HashSet<>();

    public static final List<Process> TSHARK_RUNNING_PROCESS = new ArrayList<>();

    /** cache1
     * 设备IP和DEVICE_NUMBER之间互相转换
     */
    public static final BiMap<String,String> DEVICE_IP_TO_NAME = HashBiMap.create();
}
