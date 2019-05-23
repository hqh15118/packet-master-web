package com.zjucsc.application.util;

import com.zjucsc.AttackType;
import com.zjucsc.application.config.StatisticsData;
import com.zjucsc.application.domain.bean.ThreadLocalWrapper;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CommonUtil {
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

    private static final ThreadLocal<ThreadLocalWrapper> GLOBAL_THREAD_LOCAL_MAP
            = new ThreadLocal<ThreadLocalWrapper>(){
        @Override
        protected ThreadLocalWrapper initialValue() {
            Map<String,Float> map = new HashMap<>();
//            map.put("一号左闸开",0F);
//            map.put("一号左闸关",1F);
//            map.put("二号左闸开",0F);
//            map.put("二号左闸关",1F);
//            map.put("一号右闸开",0F);
//            map.put("一号右闸关",1F);
//            map.put("二号右闸开",0F);
//            map.put("二号右闸关",1F);

//            map.put("水位0",0F);
//            map.put("水位1",0F);
//            map.put("水位2",0F);
//            map.put("水位3",0F);
//            map.put("水位4",0F);
//            map.put("水位5",0F);
//            map.put("水位6",0F);
//
//            map.put("压差0_1",0F);
//            map.put("压差1_2",0F);
//            map.put("压差2_3",0F);
//            map.put("压差3_4",0F);
//            map.put("压差4_5",0F);
//            map.put("压差5_6",0F);

//            map.put("三号左闸开",0F);
//            map.put("三号左闸关",1F);
//            map.put("三号右闸开",0F);
//            map.put("三号右闸关",1F);
//            map.put("四号左闸开",0F);
//            map.put("四号左闸关",1F);
//            map.put("四号右闸开",0F);
//            map.put("四号右闸关",1F);
//            map.put("五号左闸开",0F);
//            map.put("五号左闸关",1F);
//            map.put("五号右闸开",0F);
//            map.put("五号右闸关",1F);
//            map.put("六号左闸开",0F);
//            map.put("六号左闸关",1F);
//            map.put("六号右闸开",0F);
//            map.put("六号右闸关",1F);

//            map.put("第一个船闸状态量" , 0f);
//            map.put("第二个船闸状态量" , 0f);
//            map.put("第三个船闸状态量" , 0f);
//            map.put("第四个船闸状态量" , 0f);
//            map.put("第五个船闸状态量" , 0f);
//            map.put("第六个船闸状态量" , 0f);
//
            StatisticsData.initArtArgs("第一个船闸状态量");
            StatisticsData.initArtArgs("第二个船闸状态量");
            StatisticsData.initArtArgs("第三个船闸状态量");
            StatisticsData.initArtArgs("第四个船闸状态量");
            StatisticsData.initArtArgs("第五个船闸状态量");
            StatisticsData.initArtArgs("第六个船闸状态量");
//            StatisticsData.initArtArgs("一号左闸开");
//            StatisticsData.initArtArgs("一号左闸关");
//            StatisticsData.initArtArgs("一号右闸开");
//            StatisticsData.initArtArgs("一号右闸关");
//
//            StatisticsData.initArtArgs("二号左闸开");
//            StatisticsData.initArtArgs("二号左闸关");
//            StatisticsData.initArtArgs("二号右闸开");
//            StatisticsData.initArtArgs("二号右闸关");
//
//            StatisticsData.initArtArgs("三号左闸开");
//            StatisticsData.initArtArgs("三号左闸关");
//            StatisticsData.initArtArgs("三号右闸开");
//            StatisticsData.initArtArgs("三号右闸关");
//
//            StatisticsData.initArtArgs("四号左闸开");
//            StatisticsData.initArtArgs("四号左闸关");
//            StatisticsData.initArtArgs("四号右闸开");
//            StatisticsData.initArtArgs("四号右闸关");
//
//            StatisticsData.initArtArgs("五号左闸开");
//            StatisticsData.initArtArgs("五号左闸关");
//            StatisticsData.initArtArgs("五号右闸开");
//            StatisticsData.initArtArgs("五号右闸关");
//
//            StatisticsData.initArtArgs("六号左闸开");
//            StatisticsData.initArtArgs("六号左闸关");
//            StatisticsData.initArtArgs("六号右闸开");
//            StatisticsData.initArtArgs("六号右闸关");

            StatisticsData.initArtArgs("水位0");
            StatisticsData.initArtArgs("水位1");
            StatisticsData.initArtArgs("水位2");
            StatisticsData.initArtArgs("水位3");
            StatisticsData.initArtArgs("水位4");
            StatisticsData.initArtArgs("水位5");
            StatisticsData.initArtArgs("水位6");

            StatisticsData.initArtArgs("压差0_1");
            StatisticsData.initArtArgs("压差1_2");
            StatisticsData.initArtArgs("压差2_3");
            StatisticsData.initArtArgs("压差3_4");
            StatisticsData.initArtArgs("压差4_5");
            StatisticsData.initArtArgs("压差5_6");

            List<AttackType> attackTypes = new LinkedList<>();
            return new ThreadLocalWrapper(map,attackTypes);
        }
    };

    private static final ThreadLocal<StringBuilder> GLOBAL_THREAD_LOCAL_STRINGBUILDER =
            new ThreadLocal<StringBuilder>(){
                @Override
                protected StringBuilder initialValue() {
                    return  new StringBuilder(100);
                }
            };

    private static ConcurrentHashMap<String,Float> map = new ConcurrentHashMap<>();

    public static Map<String,Float> getGlobalArtMap(){
        //return GLOBAL_THREAD_LOCAL_MAP.get().getFloatMap();
        return map;
    }

    static {
        initMap(map);
    }

    private static void initMap(Map<String,Float> map){
//        map.put("一号左闸开",0F);
//            map.put("一号左闸关",1F);
//            map.put("二号左闸开",0F);
//            map.put("二号左闸关",1F);
//            map.put("一号右闸开",0F);
//            map.put("一号右闸关",1F);
//            map.put("二号右闸开",0F);
//            map.put("二号右闸关",1F);

        map.put("水位0",0F);
        map.put("水位1",0F);
        map.put("水位2",0F);
        map.put("水位3",0F);
        map.put("水位4",0F);
        map.put("水位5",0F);
        map.put("水位6",0F);

        map.put("压差0_1",0F);
        map.put("压差1_2",0F);
        map.put("压差2_3",0F);
        map.put("压差3_4",0F);
        map.put("压差4_5",0F);
        map.put("压差5_6",0F);

//            map.put("三号左闸开",0F);
//            map.put("三号左闸关",1F);
//            map.put("三号右闸开",0F);
//            map.put("三号右闸关",1F);
//            map.put("四号左闸开",0F);
//            map.put("四号左闸关",1F);
//            map.put("四号右闸开",0F);
//            map.put("四号右闸关",1F);
//            map.put("五号左闸开",0F);
//            map.put("五号左闸关",1F);
//            map.put("五号右闸开",0F);
//            map.put("五号右闸关",1F);
//            map.put("六号左闸开",0F);
//            map.put("六号左闸关",1F);
//            map.put("六号右闸开",0F);
//            map.put("六号右闸关",1F);

        map.put("第一个船闸状态量" , 0f);
        map.put("第二个船闸状态量" , 0f);
        map.put("第三个船闸状态量" , 0f);
        map.put("第四个船闸状态量" , 0f);
        map.put("第五个船闸状态量" , 0f);
        map.put("第六个船闸状态量" , 0f);

        StatisticsData.initArtArgs("第一个船闸状态量");
        StatisticsData.initArtArgs("第二个船闸状态量");
        StatisticsData.initArtArgs("第三个船闸状态量");
        StatisticsData.initArtArgs("第四个船闸状态量");
        StatisticsData.initArtArgs("第五个船闸状态量");
        StatisticsData.initArtArgs("第六个船闸状态量");

        StatisticsData.initArtArgs("水位0");
        StatisticsData.initArtArgs("水位1");
        StatisticsData.initArtArgs("水位2");
        StatisticsData.initArtArgs("水位3");
        StatisticsData.initArtArgs("水位4");
        StatisticsData.initArtArgs("水位5");
        StatisticsData.initArtArgs("水位6");

        StatisticsData.initArtArgs("压差0_1");
        StatisticsData.initArtArgs("压差1_2");
        StatisticsData.initArtArgs("压差2_3");
        StatisticsData.initArtArgs("压差3_4");
        StatisticsData.initArtArgs("压差4_5");
        StatisticsData.initArtArgs("压差5_6");
    }

    public static List<AttackType> getGlobalAttackList(){
        return GLOBAL_THREAD_LOCAL_MAP.get().getAttackTypeList();
    }

    public static StringBuilder getGlobalStringBuilder(){
        return GLOBAL_THREAD_LOCAL_STRINGBUILDER.get();
    }
}
