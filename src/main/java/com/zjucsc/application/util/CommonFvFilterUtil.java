package com.zjucsc.application.util;

import com.zjucsc.application.config.Common;
import com.zjucsc.application.system.entity.FvDimensionFilter;
import com.zjucsc.application.tshark.analyzer.FiveDimensionAnalyzer;
import com.zjucsc.application.tshark.filter.FiveDimensionPacketFilter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class CommonFvFilterUtil {

    public static void clearAll(){
        Common.FV_DIMENSION_FILTER_PRO.clear();
        log.info("clear all fv dimension filter of gplotid : {} " , Common.GPLOT_ID);
    }

    public synchronized static void addOrUpdateFvFilter(String deviceName , List<FvDimensionFilter> filterList , String filterName){
        FiveDimensionAnalyzer analyzer;
        if ((analyzer = Common.FV_DIMENSION_FILTER_PRO.get(deviceName))==null){
            FiveDimensionPacketFilter fiveDimensionPacketFilter = new FiveDimensionPacketFilter(filterName);
            fiveDimensionPacketFilter.setFilterList(filterList);
            analyzer = new FiveDimensionAnalyzer(fiveDimensionPacketFilter);
            Common.FV_DIMENSION_FILTER_PRO.put(deviceName,analyzer);
            log.info("add new fv dimension analyzer cause device {} is new !! NEW Filter list is : {} " , deviceName , filterList);
        }else {
            analyzer.getAnalyzer().setFilterList(filterList);
            log.info("update old fv dimension analyzer cause device {} is existed !! NEW Filter list is : {} " , deviceName , filterList);
        }
    }
}
