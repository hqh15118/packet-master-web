package com.zjucsc.application.util;

import com.zjucsc.application.statistic.StatisticsData;

import java.text.SimpleDateFormat;
import java.util.Map;

public class AppCommonUtil {


    /**
     * 用于初始化工艺参数需要的数据
     * 1.StatisticsData.initArtArgs(artArg);
     * 【ConcurrentHashMap<String, LinkedList<String>> ART_INFO】 ==> 发送给前端的
     * 2.后端解析完成后往里面put的
     * @param artArg 工艺参数名称
     */
    public static void initArtMap2Show(String artArg){
        StatisticsData.initArtMap(artArg);
    }

    public static void removeArtMap2Show(String artArg){
        StatisticsData.removeArtArgs(artArg);
    }

}
