package com.zjucsc.application.config;

import com.zjucsc.application.domain.bean.CollectorState;
import com.zjucsc.application.domain.filter.FiveDimensionPacketFilter;
import com.zjucsc.application.domain.filter.OperationPacketFilter;
import com.zjucsc.application.domain.filter.OtherPacketFilter;
import com.zjucsc.application.domain.filter.TcpPacketFilter;
import com.zjucsc.application.handler.ThreadExceptionHandler;
import com.zjucsc.application.tshark.analyzer.FiveDimensionAnalyzer;
import com.zjucsc.application.tshark.analyzer.OperationAnalyzer;
import com.zjucsc.application.tshark.analyzer.OtherPacketAnalyzer;
import com.zjucsc.application.tshark.analyzer.TcpAnalyzer;
import com.zjucsc.application.util.AbstractAnalyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;


public class Common {

    public static final int SOCKET_IO_PORT = 8081;

    public static final String CAPTURE_COMMAND_WIN = "C:\\Users\\Administrator\\Desktop\\tshark_min_win\\tshark.exe -l -n -e frame.protocols -e eth.dst -e eth.src -e ip.src -e ip.addr -e tcp.srcport -e tcp.dstport -e s7comm.param.func -e modbus.func_code -T ek -c 5 -r C:\\Users\\Administrator\\Desktop\\pcap_files\\question_1531953285_02.pcap4j";

    public static final String CAPTURE_COMMAND_MAC = "/Applications/Wireshark.app/Contents/MacOS/tshark -l -n -Y tcp -e frame.protocols -e eth.dst -e frame.cap_len -e eth.src -e ip.src -e ip.dst -e tcp.srcport -e tcp.dstport -e s7comm.param.func -e modbus.func_code -e tcp.payload -e s7comm.header.rosctr -T ek  -c 1000 -r /Users/hongqianhui/JavaProjects/packet-master-web/src/main/resources/pcap4j/question_1531953261_01.pcap4j";

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

    /** 所有要分析的协议
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
     *  String --> 对应的协议字符串
     *  AbstractAnalyzer --> 分析器<过滤器>
     */
    public static ConcurrentHashMap<String, AbstractAnalyzer> BAD_PACKET_FILTER_PRO_1 = new ConcurrentHashMap<String, AbstractAnalyzer>(){
        {
            put(PACKET_PROTOCOL.OTHER , new OtherPacketAnalyzer(new OtherPacketFilter()));
            put(PACKET_PROTOCOL.TCP , new TcpAnalyzer(new TcpPacketFilter()));
            put(PACKET_PROTOCOL.S7 , new OperationAnalyzer(new OperationPacketFilter<Integer,String>(PACKET_PROTOCOL.S7)));
            put(PACKET_PROTOCOL.S7_Ack_data , new OperationAnalyzer(new OperationPacketFilter<Integer,String>(PACKET_PROTOCOL.S7_Ack_data)));
            put(PACKET_PROTOCOL.S7_JOB , new OperationAnalyzer(new OperationPacketFilter<Integer,String>(PACKET_PROTOCOL.S7_JOB)));
            put(PACKET_PROTOCOL.MODBUS , new OperationAnalyzer(new OperationPacketFilter<Integer,String>(PACKET_PROTOCOL.MODBUS)));
            put(PACKET_PROTOCOL.FV_DIMENSION , new FiveDimensionAnalyzer(new FiveDimensionPacketFilter(PACKET_PROTOCOL.FV_DIMENSION)));
        }
    };

    /**
     * BaseResponse的返回状态 - 修改起来比较方便
     */
    public static class HTTP_STATUS_CODE{
        public static final int SYS_ERROR = 500;
        public static final int NOT_FOUND = 403;
        public static final int JSON_ERROR = 1;
    }

    //已经登录过的用户
    public final static List<String> LOGGINED_USERS = new ArrayList<>();
    //攻击的统计信息【攻击种类 - 攻击次数】
    public final static ConcurrentHashMap<String,Integer> ATTACK_TYPE_STATICS = new ConcurrentHashMap<>();

    public final static ConcurrentHashMap<Integer, CollectorState> COLLECTOR_STATE_MAP = new ConcurrentHashMap<>();


    public static final ThreadExceptionHandler COMMON_THREAD_EXCEPTION_HANDLER = new ThreadExceptionHandler();

}
