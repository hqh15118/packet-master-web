package com.zjucsc.application.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zjucsc.application.domain.bean.StatisticsDataWrapper;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ByteUtilTest {

    @Test
    public void bytesToLong(){
        byte[] bytes = new byte[]{1,2,3,4,1,2,3,4};
        System.out.println(ByteUtil.bytesToLong(bytes,0));
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
}