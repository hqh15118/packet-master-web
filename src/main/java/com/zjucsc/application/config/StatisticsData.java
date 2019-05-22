package com.zjucsc.application.config;

import com.zjucsc.application.domain.bean.GraphInfo;
import com.zjucsc.application.domain.bean.GraphInfoCollection;
import com.zjucsc.application.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

@Slf4j
public class StatisticsData {
    /**
     * 已经接收到的报文数量
     */
    public static AtomicInteger recvPacketNumber = new AtomicInteger(0);

    /**
     * 采集器的时延，key是采集器的ID，value是解析出来的最大时延
     */
    public static ConcurrentHashMap<Integer,Integer> COLLECTOR_DELAY_MAP = new ConcurrentHashMap<>();

    /**
     * 异常【既不在黑名单也不在白名单】
     */
    public static AtomicInteger exceptionNumber = new AtomicInteger(0);

    /**
     * 攻击数【黑名单里面】
     */
    public static AtomicInteger attackNumber = new AtomicInteger(0);

    public static ConcurrentHashMap<String, AtomicInteger> NUMBER_BY_DEVICE_IN = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String,AtomicInteger> NUMBER_BY_DEVICE_OUT = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String,AtomicInteger> ATTACK_BY_DEVICE = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String,AtomicInteger> EXCEPTION_BY_DEVICE = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, GraphInfoCollection> GRAPH_BY_DEVICE = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, LinkedList<String>> ART_INFO = new ConcurrentHashMap<>();

    public static void increaseNumberByDeviceIn(String deviceNumber){
        if (deviceNumber!=null) {
            doIncrease(NUMBER_BY_DEVICE_IN.get(deviceNumber) , deviceNumber , NUMBER_BY_DEVICE_IN);
        }
    }

    public static void increaseNumberByDeviceOut(String deviceNumber){
        if (deviceNumber!=null) {
            doIncrease(NUMBER_BY_DEVICE_OUT.get(deviceNumber) , deviceNumber , NUMBER_BY_DEVICE_OUT);
        }
    }
    public static void increaseAttackByDevice(String deviceNumber){
        if (deviceNumber!=null) {
            doIncrease(ATTACK_BY_DEVICE.get(deviceNumber) , deviceNumber , ATTACK_BY_DEVICE);
        }
    }
    public static void increaseExceptionByDevice(String deviceNumber){
        if (deviceNumber!=null) {
            doIncrease(EXCEPTION_BY_DEVICE.get(deviceNumber) , deviceNumber , EXCEPTION_BY_DEVICE);
        }
    }

    private static void doIncrease(AtomicInteger atomicInteger , String deviceNumber ,  ConcurrentHashMap<String, AtomicInteger> map){
        if (atomicInteger!=null) {
            atomicInteger.incrementAndGet();
        }else{
            map.put(deviceNumber , new AtomicInteger(1));
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
            timeStamps.addLast(CommonUtil.getDateFormat().format(new Date()));
        }else{
            timeStamps.addLast(CommonUtil.getDateFormat().format(new Date()));
        }
    }

    static {
        ART_INFO.put("timestamp",new LinkedList<>());
    }

    public static void initArtArgs(String artArg){
        ART_INFO.putIfAbsent(artArg,new LinkedList<>());
    }

    public static void addArtData(String artArg , String value){
        LinkedList<String> var = ART_INFO.get(artArg);
        if (var == null){
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

    public static synchronized void addArtMapData(Map<String,Float> artDataMap){
        artDataMap.forEach(new BiConsumer<String, Float>() {
            @Override
            public void accept(String artArg, Float value) {
                addArtData(artArg,String.valueOf(value));
            }
        });
    }

}
