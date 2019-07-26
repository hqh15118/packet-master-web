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
        log.info("change gplot so -> clear all fv dimension filter of gplot id : {} " , Common.GPLOT_ID);
    }

    public static void addOrUpdateFvFilter(String deviceTag , List<Rule> filterList , String filterName){
        FiveDimensionAnalyzer analyzer;
        if ((analyzer = Common.FV_DIMENSION_FILTER_PRO.get(deviceTag))==null){
            FiveDimensionPacketFilter fiveDimensionPacketFilter = new FiveDimensionPacketFilter(filterName);
            fiveDimensionPacketFilter.setFilterList(filterList);
            analyzer = new FiveDimensionAnalyzer(fiveDimensionPacketFilter);
            Common.FV_DIMENSION_FILTER_PRO.put(deviceTag,analyzer);
            //log.info("add new fv dimension analyzer cause device {} is new !! NEW Filter list is : {} [如果是空的表示该设备没有配置规则]" , deviceTag , filterList);
        }else {
            analyzer.getAnalyzer().addRules(filterList);
            //log.info("update old fv dimension analyzer cause device {} is existed !! NEW Filter list is : {} " , deviceTag , filterList);
        }
    }

    public static void removeFvFilter(String deviceTag , Rule rule){
        FiveDimensionAnalyzer analyzer = Common.FV_DIMENSION_FILTER_PRO.get(deviceTag);
        analyzer.getAnalyzer().removeRule(rule);
    }

}
