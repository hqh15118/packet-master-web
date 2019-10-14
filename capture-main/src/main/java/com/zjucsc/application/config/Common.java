package com.zjucsc.application.config;

import com.zjucsc.application.tshark.analyzer.FiveDimensionAnalyzer;
import com.zjucsc.common.handler.ThreadExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


public class Common {

    /**
     * filterStatement按照IP地址进行区分还是MAC地址进行区分
     * 0 是 IP 地址
     * 1 是 MAC 地址
     */
    public static volatile int filterStatement = 0;

    public static int GPLOT_ID = 69;

    public static final int SOCKET_IO_PORT = 8081;

    public static volatile long maxFlowInByte = Long.MAX_VALUE;

    //1 真实场景下， 0 仿真模拟
    public static int systemRunType = 1;

    /**
     * 要开启的tshark进程
     */
    public static final List<String> TSHARK_PRE_PROCESSOR_PROTOCOLS = new ArrayList<>();

    /**
     * 是否已经打开某个抓包机器上的抓包服务
     */
    public static List<String> hasStartedHost = new ArrayList<>();
    /**
     * wireshark 存储pcap文件的临时目录
     */
    public static final String WIRESHARK_TEMP_FILE = "C:\\temp";
    /**
     * cache5
     * DEVICE_TAG 五元组过滤器
     */
    public static ConcurrentHashMap<String, ConcurrentHashMap<String,FiveDimensionAnalyzer>> FV_DIMENSION_FILTER_PRO = new ConcurrentHashMap<>();

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

    public static final ThreadExceptionHandler COMMON_THREAD_EXCEPTION_HANDLER = new ThreadExceptionHandler();

    public static boolean showArtDecodeDelay = false;

}
