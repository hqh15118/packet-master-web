package com.zjucsc.application.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.system.entity.Device;
import com.zjucsc.application.system.entity.FvDimensionFilter;
import com.zjucsc.application.system.entity.Gplot;
import com.zjucsc.application.system.entity.OptFilter;
import com.zjucsc.application.system.mapper.GplotMapper;
import com.zjucsc.application.system.service.iservice.IDeviceService;
import com.zjucsc.application.system.service.iservice.IGplotService;
import com.zjucsc.application.util.CommonCacheUtil;
import com.zjucsc.application.util.CommonFvFilterUtil;
import com.zjucsc.application.util.CommonOptFilterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hongqianhui
 */
@Service("gplotservice")
public class GplotServiceImpl extends ServiceImpl<GplotMapper, Gplot> implements IGplotService {

    @Autowired private IDeviceService iDeviceService;

    @Transactional
    @Override
    public void addNewGplot(Gplot gplot) {
        Map<String,Object> removeMap = new HashMap<>();
        removeMap.put("name" , gplot.getName());
        removeByMap(removeMap);
        save(gplot);
    }

    @Transactional
    @Async(value = "global_single_thread_executor")
    @Override
    public void changeGplot(int gplotId) {
        Common.GPLOT_ID = gplotId;
        StringBuilder sb = new StringBuilder();
        List<String> deviceNumbers = iDeviceService.loadAllDevicesByGplotId(gplotId);
        for (String deviceNumber : deviceNumbers) {
            sb.delete(0 , sb.length());
            List<FvDimensionFilter> fvDimensionFilters = iDeviceService.loadAllFvDimensionFilterByDeviceNumberAndGpotId(deviceNumber,gplotId);
            CommonFvFilterUtil.addOrUpdateFvFilter(deviceNumber,fvDimensionFilters,sb.append("device_name-").append(deviceNumber).
                    append(" gplot_id-").append(gplotId).toString());
            List<OptFilter> optFilters = iDeviceService.loadAllOptFiterByDeviceNumberAndGplotId(deviceNumber,gplotId);
            CommonOptFilterUtil.addOrUpdateAnalyzer();
        }
    }

}
