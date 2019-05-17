package com.zjucsc.application.config;

import lombok.extern.slf4j.Slf4j;

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

    public static ConcurrentHashMap<String, AtomicLong> NUMBER_BY_DEVICE_IN = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String,AtomicLong> NUMBER_BY_DEVICE_OUT = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String,AtomicLong> ATTACK_BY_DEVICE = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String,AtomicLong> EXCEPTION_BY_DEVICE = new ConcurrentHashMap<>();

    public static void increaseNumberByDeviceIn(String deviceNumber){
        if (deviceNumber!=null) {
            doIncrease(NUMBER_BY_DEVICE_IN.get(deviceNumber));
        }
    }

    public static void increaseNumberByDeviceOut(String deviceNumber){
        if (deviceNumber!=null) {
            doIncrease(NUMBER_BY_DEVICE_OUT.get(deviceNumber));
        }
    }
    public static void increaseAttackByDevice(String deviceNumber){
        if (deviceNumber!=null) {
            doIncrease(ATTACK_BY_DEVICE.get(deviceNumber));
        }
    }
    public static void increaseExceptionByDevice(String deviceNumber){
        if (deviceNumber!=null) {
            doIncrease(EXCEPTION_BY_DEVICE.get(deviceNumber));
        }
    }

    private static void doIncrease(AtomicLong atomicInteger){
        if (atomicInteger!=null) {
            atomicInteger.incrementAndGet();
        }
    }
}
