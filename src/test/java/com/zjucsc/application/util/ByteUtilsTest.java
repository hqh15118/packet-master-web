package com.zjucsc.application.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zjucsc.application.domain.bean.CollectorDelay;
import com.zjucsc.application.domain.bean.StatisticsDataWrapper;
import org.hibernate.validator.constraints.EAN;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.*;

public class ByteUtilsTest {

    @Test
    public void bytesToLong(){
        byte[] bytes = new byte[]{1,2,3,4,1,2,3,4};
        System.out.println(ByteUtils.bytesToLong(bytes,0));
    }

    public  AtomicInteger recvPacketNumber = new AtomicInteger(0);

    /**
     * 采集器的时延，key是采集器的ID，value是解析出来的最大时延
     */
    public  ConcurrentHashMap<Integer,Integer> COLLECTOR_DELAY_MAP = new ConcurrentHashMap<>();

    /**
     * 异常【既不在黑名单也不在白名单】
     */
    public  AtomicInteger exceptionNumber = new AtomicInteger(0);

    /**
     * 攻击数【黑名单里面】
     */
    public  AtomicInteger attackNumber = new AtomicInteger(0);

    public  ConcurrentHashMap<String, AtomicLong> NUMBER_BY_DEVICE_IN = new ConcurrentHashMap<>();
    public  ConcurrentHashMap<String,AtomicLong> NUMBER_BY_DEVICE_OUT = new ConcurrentHashMap<>();
    public  ConcurrentHashMap<String,AtomicLong> ATTACK_BY_DEVICE = new ConcurrentHashMap<>();
    public  ConcurrentHashMap<String,AtomicLong> EXCEPTION_BY_DEVICE = new ConcurrentHashMap<>();
    public Map<String,Integer> DELAY = new HashMap<>();

    @Test
    public void decode(){
        recvPacketNumber.set(21);
        exceptionNumber.set(5);
        attackNumber.set(6);
        DELAY.put("device1",10);
        DELAY.put("device2",20);
        NUMBER_BY_DEVICE_IN.put("device1",new AtomicLong(10));
        NUMBER_BY_DEVICE_IN.put("device2",new AtomicLong(20));
        NUMBER_BY_DEVICE_OUT.put("device1",new AtomicLong(11));
        NUMBER_BY_DEVICE_OUT.put("device2",new AtomicLong(21));
        ATTACK_BY_DEVICE.put("device1",new AtomicLong(6));
        ATTACK_BY_DEVICE.put("device2",new AtomicLong(5));
        EXCEPTION_BY_DEVICE.put("device1",new AtomicLong(5));
        EXCEPTION_BY_DEVICE.put("device2",new AtomicLong(6));
        StatisticsDataWrapper statisticsDataWrapper = new StatisticsDataWrapper.Builder()
                .setNumber(recvPacketNumber.get())
                .setCollectorDelay(DELAY)
                .setExceptionCount(exceptionNumber.get())
                .setAttackCount(attackNumber.get())
                .setAttackByDevice(ATTACK_BY_DEVICE)
                .setExceptionByDevice(EXCEPTION_BY_DEVICE)
                .setNumberByDeviceIn(NUMBER_BY_DEVICE_IN)
                .setNumberByDeviceOut(NUMBER_BY_DEVICE_OUT)
                .build();
        System.out.println(JSON.toJSONString(statisticsDataWrapper , SerializerFeature.PrettyFormat));
    }
}