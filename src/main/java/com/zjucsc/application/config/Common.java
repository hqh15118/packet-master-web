package com.zjucsc.application.config;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.zjucsc.application.domain.analyzer.FiveDimensionAnalyzer;
import com.zjucsc.application.domain.analyzer.OperationAnalyzer;
import com.zjucsc.application.domain.bean.CollectorState;
import com.zjucsc.application.domain.filter.FiveDimensionPacketFilter;
import com.zjucsc.application.domain.filter.OperationPacketFilter;
import com.zjucsc.application.handler.ThreadExceptionHandler;
import com.zjucsc.application.util.AbstractAnalyzer;
import org.apache.poi.sl.draw.BitmapImageRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    /** 所有可配置的协议
     * 协议 --> 功能码 以及 对应的含义 --> 从serviceLoader中加载
     */

    public static final HashMap<String , HashMap<Integer,String>> CONFIGURATION_MAP = new HashMap<>();

    /* 所有要分析的协议
     *  过滤器种类 -> [功能码配置组]
     */
    /*public static ConcurrentHashMap<FilterType, OperationPacketFilter> BAD_PACKET_FILTER = new ConcurrentHashMap<FilterType, OperationPacketFilter>(){
        {
            put(FilterType.PROTOCOL,new OperationPacketFilter("protocol_filter"));
            put(FilterType.OPERATION,new OperationPacketFilter("operation_filter"));
            put(FilterType.ARTIFACT_PARAM,new OperationPacketFilter("artifact_filter"));
            put(FilterType.PORT,new OperationPacketFilter("port_filter"));
        }
    */

    /*  所有要分析的协议
     *  过滤器种类 -> [分析器]
     */
    /*
    public static ConcurrentHashMap<FilterType, AbstractAnalyzer> BAD_PACKET_FILTER_PRO = new ConcurrentHashMap<FilterType, AbstractAnalyzer>(){
        {
            put(OPERATION_S7Comm_JOB , new OperationAnalyzer(new OperationPacketFilter<Integer,String>(OPERATION_S7Comm_JOB.name())));
            put(FilterType.OPERATION_S7Comm_Ack_data , new OperationAnalyzer(new OperationPacketFilter<Integer,String>(OPERATION_S7Comm_Ack_data.name())));
            put(FilterType.OPERATION_MODBUS , new OperationAnalyzer(new OperationPacketFilter<Integer,String>(OPERATION_MODBUS.name())));
            put(FilterType.FIVE_DIMENSION , new FiveDimensionAnalyzer(new FiveDimensionPacketFilter(FIVE_DIMENSION.name())));
        }
    };
    */

    /**
     * Integer 设备ID
     * String -> 协议
     * OperationAnalyzer -> 报文操作分析器
     */
    public static ConcurrentHashMap<Integer,ConcurrentHashMap<String, OperationAnalyzer>> OPERATION_FILTER =
            new ConcurrentHashMap<>();
    /**
     * 设备ID
     */
    public static ConcurrentHashMap<Integer,FiveDimensionAnalyzer> FV_DIMENSION_FILTER = new ConcurrentHashMap<Integer,FiveDimensionAnalyzer>(){

    };

    /*
    public static ConcurrentHashMap<Integer, AbstractAnalyzer> BAD_PACKET_FILTER_PRO_2 = new ConcurrentHashMap<Integer, AbstractAnalyzer>(){
        {
            put(PACKET_PROTOCOL.OTHER_ID , new OtherPacketAnalyzer(new OtherPacketFilter()));
            put(PACKET_PROTOCOL.TCP_ID , new TcpAnalyzer(new TcpPacketFilter()));
            put(PACKET_PROTOCOL.S7_ID , new OperationAnalyzer(new OperationPacketFilter<Integer,String>(PACKET_PROTOCOL.S7)));
            put(PACKET_PROTOCOL.S7_Ack_data_ID , new OperationAnalyzer(new OperationPacketFilter<Integer,String>(PACKET_PROTOCOL.S7_Ack_data)));
            put(PACKET_PROTOCOL.S7_JOB_ID , new OperationAnalyzer(new OperationPacketFilter<Integer,String>(PACKET_PROTOCOL.S7_JOB)));
            put(PACKET_PROTOCOL.MODBUS_ID , new OperationAnalyzer(new OperationPacketFilter<Integer,String>(PACKET_PROTOCOL.MODBUS)));
            put(PACKET_PROTOCOL.FV_DIMENSION_ID, new FiveDimensionAnalyzer(new FiveDimensionPacketFilter(PACKET_PROTOCOL.FV_DIMENSION)));
        }
    };
    */

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
}
