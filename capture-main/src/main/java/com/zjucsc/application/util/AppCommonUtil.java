package com.zjucsc.application.util;

import com.zjucsc.application.config.StatisticsData;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AppCommonUtil {
    private static final ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT_THREAD_LOCAL
             = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("HH:mm:ss");
        }
    };
    public static SimpleDateFormat getDateFormat(){
        return SIMPLE_DATE_FORMAT_THREAD_LOCAL.get();
    }

    private static ConcurrentHashMap<String,Float> map = new ConcurrentHashMap<>();

    /**
     * 工艺参数Map，塞到每个art decoder中的map，用来填充工艺参数的
     * 【工艺参数名称，工艺参数数据】
     * @return map
     */
    public static Map<String,Float> getGlobalArtMap(){
        return map;
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
        StatisticsData.initArtArgs(artArg);
        map.put(artArg,0F);
    }

    public static void removeArtMap(String artArg){
        StatisticsData.removeArtArgs(artArg);
        map.remove(artArg);
    }

}
