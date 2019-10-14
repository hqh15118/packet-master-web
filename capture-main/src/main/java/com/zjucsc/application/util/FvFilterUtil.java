package com.zjucsc.application.util;

import com.zjucsc.application.config.Common;
import com.zjucsc.application.domain.bean.Rule;
import com.zjucsc.application.tshark.analyzer.FiveDimensionAnalyzer;
import com.zjucsc.application.tshark.filter.FiveDimensionPacketFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Slf4j
public class FvFilterUtil {

    public static void removeAllFvFilter(){
        Common.FV_DIMENSION_FILTER_PRO.clear();
    }

    /**
     *
     * @param deviceTag 目的设备
     * @param filterList 规则列表
     * @param filterName =-=没用
     */
    public static void addOrUpdateFvFilter(String deviceTag , List<Rule> filterList , String filterName){
        ConcurrentHashMap<String,FiveDimensionAnalyzer> map =
                Common.FV_DIMENSION_FILTER_PRO.computeIfAbsent(deviceTag, s -> new ConcurrentHashMap<>());
        map.clear();
        FiveDimensionAnalyzer analyzer;

        for (Rule rule : filterList) {
            String srcTag = getSrcTagOfRule(rule);
            analyzer = map.putIfAbsent(srcTag,createNewFvDimensionAnalyzer(srcTag,rule));
            if (analyzer != null){
                analyzer.getAnalyzer().addRules(Collections.singletonList(rule));
            }

        }
    }

    private static FiveDimensionAnalyzer createNewFvDimensionAnalyzer(String filterName,Rule rule){
        FiveDimensionPacketFilter fiveDimensionPacketFilter = new FiveDimensionPacketFilter(filterName);
        fiveDimensionPacketFilter.setFilterList(Collections.singletonList(rule));
        return new FiveDimensionAnalyzer(fiveDimensionPacketFilter);
    }

    private static String getSrcTagOfRule(Rule rule){
        if (!rule.getFvDimensionFilter().getSrcIp().equals("--")){
            return rule.getFvDimensionFilter().getSrcIp();
        }
        return rule.getFvDimensionFilter().getSrcMac();
    }

    public static void removeFvFilter(String deviceTag , Rule rule){
        Map<String,FiveDimensionAnalyzer> analyzerMap = Common.FV_DIMENSION_FILTER_PRO.get(deviceTag);
        String srcTag = getSrcTagOfRule(rule);
        analyzerMap.get(srcTag).getAnalyzer().removeRule(rule);
    }

    public static void disableDeviceAllConfig(String deviceTag){
        Common.FV_DIMENSION_FILTER_PRO.remove(deviceTag);
    }

}
