package com.zjucsc.attack.util;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-30 - 22:48
 */
public class AttackAnalyzeUtil {

    /*********************************
     *
     * 统一以【纳秒】为单位
     *
     ********************************/


    /**
     * @param oldTimeStamp 旧报文时间戳 ns
     * @param newTimeStamp 新报文时间戳 ns
     * @param thresholdTime 阈值，新 - 旧 > 该值，表示在一定时间内没有重复发生，正常；反之不正常 ns
     * @return false 表示不正常，true表示正常
     */
    public static boolean timeDifferenceAnalyze(String oldTimeStamp,String newTimeStamp,long thresholdTime){
        long oldTime = Long.parseLong(oldTimeStamp);
        long newTime = Long.parseLong(newTimeStamp);
        if ((newTime - oldTime) > thresholdTime){
            return true;
        }else{
            return false;
        }
    }
}
