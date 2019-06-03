package com.zjucsc.application.util;

import com.zjucsc.application.system.art.CommonBiConsumer;
import com.zjucsc.art_decode.other.AttackType;
import com.zjucsc.application.config.StatisticsData;
import com.zjucsc.application.domain.bean.ThreadLocalWrapper;

import java.text.SimpleDateFormat;
import java.util.*;
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

    private static final ThreadLocal<CommonBiConsumer> BI_CONSUMER_THREAD_LOCAL
            = new ThreadLocal<CommonBiConsumer>(){
        @Override
        protected CommonBiConsumer initialValue() {
            return new CommonBiConsumer(map);
        }
    };

    private static final ThreadLocal<ThreadLocalWrapper> GLOBAL_THREAD_LOCAL_MAP
            = new ThreadLocal<ThreadLocalWrapper>(){
        @Override
        protected ThreadLocalWrapper initialValue() {
            Map<String,Float> map = new HashMap<>();
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

            List<AttackType> attackTypes = new LinkedList<>();
            return new ThreadLocalWrapper(map,attackTypes);
        }
    };

    private static ConcurrentHashMap<String,Float> map = new ConcurrentHashMap<>();

    /**
     * 工艺参数Map，塞到每个art decoder中的map，用来填充工艺参数的
     * 【工艺参数名称，工艺参数数据】
     * @return map
     */
    public static Map<String,Float> getGlobalArtMap(){
        return map;
    }

    static {
        initMap(map);
    }

    private static void initMap(Map<String,Float> map){
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

    /**
     * 用于初始化工艺参数需要的数据
     * 1.StatisticsData.initArtArgs(artArg);
     * 【ConcurrentHashMap<String, LinkedList<String>> ART_INFO】 ==> 发送给前端的
     * 2.map.put(artArg,0F);
     * 【ConcurrentHashMap<String,Float> map】 ==> 用于记录
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

    public static List<AttackType> getGlobalAttackList(){
        return GLOBAL_THREAD_LOCAL_MAP.get().getAttackTypeList();
    }

    public static CommonBiConsumer getThreadLocalBiConsumer(){
        return BI_CONSUMER_THREAD_LOCAL.get();
    }
}
