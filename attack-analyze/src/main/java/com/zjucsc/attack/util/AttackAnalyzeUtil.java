package com.zjucsc.attack.util;

import com.zjucsc.attack.analyze.analyzer.CositeDOSAttackAnalyzer;
import com.zjucsc.attack.analyze.analyzer.MultisiteDOSAttackAnalyzer;
import com.zjucsc.attack.analyze.analyzer_util.MultisiteDOSAttackAnalyzeList;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-30 - 22:48
 */
public class AttackAnalyzeUtil {

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
