package com.zjucsc.packetmasterweb.util;

import com.alibaba.fastjson.JSON;
import com.zjucsc.base.BaseResponse;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;

public class Util {


    public static void showJSON( String comment ,  BaseResponse baseResponse){
        System.out.println(comment);
        System.out.println(JSON.toJSONString(baseResponse));
    }

}
