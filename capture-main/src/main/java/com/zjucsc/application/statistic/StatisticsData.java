package com.zjucsc.application.statistic;

import com.zjucsc.application.domain.bean.GraphInfo;
import com.zjucsc.application.domain.bean.GraphInfoCollection;
import com.zjucsc.application.domain.non_hessian.ArtGroupWrapper;
import com.zjucsc.application.domain.non_hessian.DeviceMaxFlow;
import com.zjucsc.application.util.AppCommonUtil;
import com.zjucsc.socket_io.SocketIoEvent;
import com.zjucsc.socket_io.SocketServiceCenter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Hash;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

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
     * 攻击数【黑名单里面】
     */
    public static AtomicInteger attackNumber = new AtomicInteger(0);

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

    private static ConcurrentHashMap<String,Float> GLOBAL_ART_INFO = new ConcurrentHashMap<>();
    /**
     * 工艺参数Map，塞到每个art decoder中的map，用来填充工艺参数的
     * 【工艺参数名称，工艺参数数据】
     * @return map
     */
    public static Map<String,Float> getGlobalArtMap(){
        return GLOBAL_ART_INFO;
    }
    /**
     * key 是工艺参数的名字
     */
    public static final ConcurrentHashMap<String, String> ART_INFO = new ConcurrentHashMap<>();
    //public static final ConcurrentHashMap<String, LinkedList<String>> ART_INFO_SEND = new ConcurrentHashMap<>();//只发送需要显示的个工艺参数
    public static final ConcurrentHashMap<String, ArtGroupWrapper> ART_INFO_SEND_SINGLE = new ConcurrentHashMap<>();

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
        GraphInfoCollection collection = GRAPH_BY_DEVICE.computeIfAbsent(deviceNumber, s -> new GraphInfoCollection());
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

    public static void removeArtArgs(String artArg){
        ART_INFO.remove(artArg);
    }

    /**
     * 更新要发送到前端的工艺参数MAP
     * @param artArg 工艺参数名称
     * @param value 工艺参数值 string类型
     */
    public static void addOrUpdateArtData(String artArg , String value){
        ART_INFO.put(artArg, value);
    }


    /*********************************
     *
     *  STATISTICS PROTOCOL NUM e.g. [S7comm,20] [Modbus,30]
     *
     **********************************/
    public static void addProtocolNum(String protocolName,int deltaNumber){
        AtomicLong atomicLong = PROTOCOL_NUM.computeIfAbsent(protocolName,
                s -> new AtomicLong(0));
        atomicLong.addAndGet(deltaNumber);
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
        if (ALL_IP_ADDRESS.put(ipAddress,"") == null){
            SocketServiceCenter.updateAllClient(SocketIoEvent.NEW_IP,ipAddress);
        }
    }

    public static Collection<String> getAllIpCollections(){
        return ALL_IP_ADDRESS.keySet();
    }

    /**
     * 用于初始化工艺参数需要的数据
     * 1.StatisticsData.initArtArgs(artArg);
     * 【ConcurrentHashMap<String, LinkedList<String>> ART_INFO】 ==> 发送给前端的
     * 2.map.put(artArg,0F);
     * 【ConcurrentHashMap<String,Float> map】 ==> 用于记录实时的参数
     * @param artArg
     */
    public static void initArtMap(String artArg){
        /*
         * 新增工艺参数的时候，前端推送也需要增加一个参数，并将其初始化为0
         * @param artArg 工艺参数名称
         */
        ART_INFO.putIfAbsent(artArg,"0");
        /*
         * 全局的工艺参数map，value是float类型
         */
        GLOBAL_ART_INFO.put(artArg,0F);
    }

    public static void removeArtMap(String artArg){
        StatisticsData.removeArtArgs(artArg);
        GLOBAL_ART_INFO.remove(artArg);
    }
}
