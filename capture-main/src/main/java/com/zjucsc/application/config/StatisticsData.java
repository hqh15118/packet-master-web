package com.zjucsc.application.config;

import com.zjucsc.application.domain.bean.GraphInfo;
import com.zjucsc.application.domain.bean.GraphInfoCollection;
import com.zjucsc.application.domain.non_hessian.DeviceMaxFlow;
import com.zjucsc.application.util.AppCommonUtil;
import com.zjucsc.socket_io.SocketIoEvent;
import com.zjucsc.socket_io.SocketServiceCenter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class StatisticsData {
    /**
     * 已经接收到的报文数量
     */
    public static AtomicInteger recvPacketNumber = new AtomicInteger(0);

    /**
     * 所有已经捕获到的协议及其数量
     */
    private static final ConcurrentHashMap<String, AtomicLong> PROTOCOL_NUM = new ConcurrentHashMap<>();

    /**
     * 采集器的时延，key是采集器的ID，value是解析出来的最大时延
     */
    public static ConcurrentHashMap<Integer,Integer> COLLECTOR_DELAY_MAP = new ConcurrentHashMap<>();

    /**
     * 报文流量
     */
    public static final AtomicInteger FLOW = new AtomicInteger(0);

    /**
     * 所有IP地址的统计集合
     */
    private static final ConcurrentHashMap<String,String> ALL_IP_ADDRESS = new ConcurrentHashMap<>();

    /**
     * 异常【既不在黑名单也不在白名单】
     */
    public static AtomicInteger exceptionNumber = new AtomicInteger(0);

    /**
     * 攻击数【黑名单里面】
     */
    public static AtomicInteger attackNumber = new AtomicInteger(0);

    private static final HashMap<String, DeviceMaxFlow> DEVICE_MAX_FLOW = new HashMap<>();

    public static DeviceMaxFlow getDeviceMaxFlow(String deviceNumber){
        return DEVICE_MAX_FLOW.get(deviceNumber);
    }
    public static void addDeviceMaxFlowConfig(DeviceMaxFlow deviceMaxFlow){
        DEVICE_MAX_FLOW.put(deviceMaxFlow.getDeviceNumber(),deviceMaxFlow);
    }
    /**
     * key 是 设备名 deviceNumber
     */
    public static final ConcurrentHashMap<String, AtomicInteger> NUMBER_BY_DEVICE_IN = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String,AtomicInteger> NUMBER_BY_DEVICE_OUT = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String, AtomicInteger> FLOW_BY_DEVICE_IN = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String,AtomicInteger> FLOW_BY_DEVICE_OUT = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String,AtomicInteger> ATTACK_BY_DEVICE = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String,AtomicInteger> EXCEPTION_BY_DEVICE = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String, GraphInfoCollection> GRAPH_BY_DEVICE = new ConcurrentHashMap<>();
    /**
     * key 是工艺参数的名字
     */
    public static final ConcurrentHashMap<String, LinkedList<String>> ART_INFO = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String, LinkedList<String>> ART_INFO_SEND = new ConcurrentHashMap<>();//只发送需要显示的个工艺参数
    public static final ConcurrentHashMap<String,String> ART_INFO_SEND_SINGLE = new ConcurrentHashMap<>();

    public static void increaseNumberByDeviceIn(String deviceNumber,int delta){
        if (deviceNumber!=null) {
            doIncrease(NUMBER_BY_DEVICE_IN.get(deviceNumber) , deviceNumber , NUMBER_BY_DEVICE_IN,delta);
        }
    }

    public static void increaseFlowByDeviceIn(String deviceNumber,int delta){
        if (deviceNumber!=null){
            doIncrease(FLOW_BY_DEVICE_IN.get(deviceNumber) , deviceNumber , FLOW_BY_DEVICE_IN,delta);
        }
    }

    public static void increaseFlowByDeviceOut(String deviceNumber,int delta){
        if (deviceNumber!=null){
            doIncrease(FLOW_BY_DEVICE_OUT.get(deviceNumber) , deviceNumber , FLOW_BY_DEVICE_OUT,delta);
        }
    }

    public static void increaseNumberByDeviceOut(String deviceNumber , int delta){
        if (deviceNumber!=null) {
            doIncrease(NUMBER_BY_DEVICE_OUT.get(deviceNumber) , deviceNumber , NUMBER_BY_DEVICE_OUT , delta);
        }
    }
    public static void increaseAttackByDevice(String deviceNumber){
        if (deviceNumber!=null) {
            doIncrease(ATTACK_BY_DEVICE.get(deviceNumber) , deviceNumber , ATTACK_BY_DEVICE , 1);
        }
    }
    public static void increaseExceptionByDevice(String deviceNumber){
        if (deviceNumber!=null) {
            doIncrease(EXCEPTION_BY_DEVICE.get(deviceNumber) , deviceNumber , EXCEPTION_BY_DEVICE , 1);
        }
    }

    private static void doIncrease(AtomicInteger atomicInteger , String deviceNumber ,  ConcurrentHashMap<String, AtomicInteger> map
    ,int delta){
        if (atomicInteger!=null) {
            atomicInteger.addAndGet(delta);
        }else{
            map.put(deviceNumber , new AtomicInteger(delta));
        }
    }

    public static void addDeviceGraphInfo(String deviceNumber , GraphInfo info){
        GraphInfoCollection collection = GRAPH_BY_DEVICE.get(deviceNumber);
        if (collection == null){
            collection = new GraphInfoCollection();
            GRAPH_BY_DEVICE.put(deviceNumber , collection);
        }
        doAddInfoToList(collection.getAttack(),info.getAttack());
        doAddInfoToList(collection.getDelay(),info.getDelay());
        doAddInfoToList(collection.getException(),info.getException());
        doAddInfoToList(collection.getPacketIn(),info.getPacketIn());
        doAddInfoToList(collection.getPacketOut(),info.getPacketOut());
        addTimeStamp(collection.getTimeStamp());
    }

    private static void doAddInfoToList(LinkedList<Integer> list , int data){
        if (list.size() >= 12){
            list.removeFirst();
            list.addLast(data);
        }else{
            list.addLast(data);
        }
    }

    private static void addTimeStamp(LinkedList<String> timeStamps){
        if (timeStamps.size() >= 12){
            timeStamps.removeFirst();
            timeStamps.addLast(AppCommonUtil.getDateFormat().format(new Date()));
        }else{
            timeStamps.addLast(AppCommonUtil.getDateFormat().format(new Date()));
        }
    }

    static {
        ART_INFO.put("timestamp",new LinkedList<>());
    }

    public static void initArtArgs(String artArg){
        ART_INFO.putIfAbsent(artArg,new LinkedList<>());
    }

    public static void removeArtArgs(String artArg){
        ART_INFO.remove(artArg);
    }

    public static final byte[] LINKED_LIST_LOCK = new byte[1];

    public static void addArtData(String artArg , String value){
        synchronized (LINKED_LIST_LOCK){
            LinkedList<String> var = ART_INFO.get(artArg);
        if (var == null){
            //需要先调用AppCommonUtil.initArtMap
            log.error("工艺参数 {} 未初始化添加到ART_INFO中，请修正" , artArg);
        }else{
                if (var.size() >= 6){
                    var.removeFirst();
                    var.addLast(value);
                }else{
                    var.addLast(value);
                }
            }
        }
    }

    public static synchronized void addArtMapData(Map<String,Float> artDataMap){
        artDataMap.forEach((artArg, value) -> addArtData(artArg,String.valueOf(value)));
    }

    /*********************************
     *
     *  STATISTICS  PROTOCOL NUM e.g. [S7comm,20] [Modbus,30]
     *
     **********************************/
    public static void addProtocolNum(String protocolName,int deltaNumber){
        AtomicLong atomicLong = PROTOCOL_NUM.get(protocolName);
        if (atomicLong == null){
            PROTOCOL_NUM.putIfAbsent(protocolName,new AtomicLong(1));
        }else{
            atomicLong.addAndGet(deltaNumber);
        }
    }

    public static Map<String,AtomicLong> getProtocolNum(){
        return PROTOCOL_NUM;
    }

    /*********************************
     *
     *  STATISTICS ALL IP ADDRESS
     *
     **********************************/
    public static void statisticAllIpAddress(String ipAddress){
        if (ALL_IP_ADDRESS.put(ipAddress,"")==null){
            SocketServiceCenter.updateAllClient(SocketIoEvent.NEW_IP,ipAddress);
        }
    }

    public static Collection<String> getAllIpCollections(){
        return ALL_IP_ADDRESS.keySet();
    }

    /**
     * 清除所有的统计数据
     */
    public static void removeTargetDeviceStatisticsData(){
        NUMBER_BY_DEVICE_IN.clear();
        NUMBER_BY_DEVICE_OUT.clear();
        FLOW_BY_DEVICE_OUT.clear();
        FLOW_BY_DEVICE_IN.clear();
        ATTACK_BY_DEVICE.clear();
        EXCEPTION_BY_DEVICE.clear();
        GRAPH_BY_DEVICE.clear();
        ART_INFO.clear();
        ART_INFO_SEND_SINGLE.clear();
        recvPacketNumber.set(0);
        PROTOCOL_NUM.clear();
        COLLECTOR_DELAY_MAP.clear();
        FLOW.set(0);
        ALL_IP_ADDRESS.clear();
        exceptionNumber.set(0);
        attackNumber.set(0);
        DEVICE_MAX_FLOW.clear();
    }
}
