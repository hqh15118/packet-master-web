package com.zjucsc.application.util;

import com.zjucsc.application.config.StatisticsData;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

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

    private static final ThreadLocal<Map<String,Float>> GLOBAL_THREAD_LOCAL_MAP
            = new ThreadLocal<Map<String,Float>>(){
        @Override
        protected Map<String, Float> initialValue() {
            Map<String,Float> map = new HashMap<>();
            map.put("一号左闸开",0F);
            map.put("一号左闸关",1F);
            map.put("二号左闸开",0F);
            map.put("二号左闸关",1F);
            map.put("一号右闸开",0F);
            map.put("一号右闸关",1F);
            map.put("二号右闸开",0F);
            map.put("二号右闸关",1F);

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

            map.put("三号左闸开",0F);
            map.put("三号左闸关",1F);
            map.put("三号右闸开",0F);
            map.put("三号右闸关",1F);
            map.put("四号左闸开",0F);
            map.put("四号左闸关",1F);
            map.put("四号右闸开",0F);
            map.put("四号右闸关",1F);
            map.put("五号左闸开",0F);
            map.put("五号左闸关",1F);
            map.put("五号右闸开",0F);
            map.put("五号右闸关",1F);
            map.put("六号左闸开",0F);
            map.put("六号左闸关",1F);
            map.put("六号右闸开",0F);
            map.put("六号右闸关",1F);

            StatisticsData.initArtArgs("一号左闸开");
            StatisticsData.initArtArgs("一号左闸关");
            StatisticsData.initArtArgs("一号右闸开");
            StatisticsData.initArtArgs("一号右闸关");

            StatisticsData.initArtArgs("二号左闸开");
            StatisticsData.initArtArgs("二号左闸关");
            StatisticsData.initArtArgs("二号右闸开");
            StatisticsData.initArtArgs("二号右闸关");

            StatisticsData.initArtArgs("三号左闸开");
            StatisticsData.initArtArgs("三号左闸关");
            StatisticsData.initArtArgs("三号右闸开");
            StatisticsData.initArtArgs("三号右闸关");

            StatisticsData.initArtArgs("四号左闸开");
            StatisticsData.initArtArgs("四号左闸关");
            StatisticsData.initArtArgs("四号右闸开");
            StatisticsData.initArtArgs("四号右闸关");

            StatisticsData.initArtArgs("五号左闸开");
            StatisticsData.initArtArgs("五号左闸关");
            StatisticsData.initArtArgs("五号右闸开");
            StatisticsData.initArtArgs("五号右闸关");

            StatisticsData.initArtArgs("六号左闸开");
            StatisticsData.initArtArgs("六号左闸关");
            StatisticsData.initArtArgs("六号右闸开");
            StatisticsData.initArtArgs("六号右闸关");

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

            return map;
        }
    };

//    private static void doInitMap(Map<String,Float> map , String artArg,Float value){
//
//    }

    public static Map<String,Float> getGlobalArtMap(){
        return GLOBAL_THREAD_LOCAL_MAP.get();
    }
}
