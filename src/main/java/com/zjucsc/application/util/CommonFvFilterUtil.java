package com.zjucsc.application.util;

import com.zjucsc.application.config.Common;
import com.zjucsc.application.system.entity.FvDimensionFilter;
import com.zjucsc.application.tshark.analyzer.FiveDimensionAnalyzer;
import com.zjucsc.application.tshark.filter.FiveDimensionPacketFilter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class CommonFvFilterUtil {

    public static void removeAllFvFilter(){
        Common.FV_DIMENSION_FILTER_PRO.clear();
        log.info("clear all fv dimension filter of gplotid : {} " , Common.GPLOT_ID);
    }

    public synchronized static void addOrUpdateFvFilter(String deviceIp , List<FvDimensionFilter> filterList , String filterName){
        FiveDimensionAnalyzer analyzer;
        if ((analyzer = Common.FV_DIMENSION_FILTER_PRO.get(deviceIp))==null){
            FiveDimensionPacketFilter fiveDimensionPacketFilter = new FiveDimensionPacketFilter(filterName);
            fiveDimensionPacketFilter.setFilterList(filterList);
            analyzer = new FiveDimensionAnalyzer(fiveDimensionPacketFilter);
            Common.FV_DIMENSION_FILTER_PRO.put(deviceIp,analyzer);
            log.info("add new fv dimension analyzer cause device {} is new !! NEW Filter list is : {} [如果是空的表示该设备没有配置规则]" , deviceIp , filterList);
        }else {
            analyzer.getAnalyzer().setFilterList(filterList);
            log.info("update old fv dimension analyzer cause device {} is existed !! NEW Filter list is : {} " , deviceIp , filterList);
        }
    }

}
