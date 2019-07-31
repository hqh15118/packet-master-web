package com.zjucsc.application.util;

import com.zjucsc.application.config.Common;
import com.zjucsc.application.domain.bean.Rule;
import com.zjucsc.application.tshark.analyzer.FiveDimensionAnalyzer;
import com.zjucsc.application.tshark.filter.FiveDimensionPacketFilter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class CommonFvFilterUtil {

    public static void removeAllFvFilter(){
        Common.FV_DIMENSION_FILTER_PRO.clear();
    }

    public static void addOrUpdateFvFilter(String deviceTag , List<Rule> filterList , String filterName){
        FiveDimensionAnalyzer analyzer;
        if ((analyzer = Common.FV_DIMENSION_FILTER_PRO.get(deviceTag))==null){
            FiveDimensionPacketFilter fiveDimensionPacketFilter = new FiveDimensionPacketFilter(filterName);
            fiveDimensionPacketFilter.setFilterList(filterList);
            analyzer = new FiveDimensionAnalyzer(fiveDimensionPacketFilter);
            Common.FV_DIMENSION_FILTER_PRO.put(deviceTag,analyzer);
        }else {
            analyzer.getAnalyzer().addRules(filterList);
        }
    }

    public static void removeFvFilter(String deviceTag , Rule rule){
        FiveDimensionAnalyzer analyzer = Common.FV_DIMENSION_FILTER_PRO.get(deviceTag);
        analyzer.getAnalyzer().removeRule(rule);
    }

    public static void disableDeviceAllConfig(String deviceTag){
        Common.FV_DIMENSION_FILTER_PRO.remove(deviceTag);
    }

}
