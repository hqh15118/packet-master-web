package com.zjucsc.application.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.domain.bean.DeviceNumberAndIp;
import com.zjucsc.application.domain.exceptions.ProtocolIdNotValidException;
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
    public void changeGplot(int gplotId) throws ProtocolIdNotValidException {
        Common.GPLOT_ID = gplotId;
        StringBuilder sb = new StringBuilder();
        //移除旧组态图上的所有规则
        CommonOptFilterUtil.removeAllOptFilter();
        CommonFvFilterUtil.removeAllFvFilter();
        //移除旧组态图上的所有DEVICE_NUMBER和DEVICE_IP之间的对应关系
        CommonCacheUtil.removeAllCachedDeviceNumber();
        //reload filters
        List<DeviceNumberAndIp> deviceNumbers = iDeviceService.loadAllDevicesByGplotId(gplotId);   //load all device from device_info table by gplot_id
        for (DeviceNumberAndIp deviceNumberAndIp : deviceNumbers) {
            sb.delete(0 , sb.length());
            //load all fv dimension rule from fv_dimension table by device_number + gpolt_id
            List<FvDimensionFilter> fvDimensionFilters = iDeviceService.
                    loadAllFvDimensionFilterByDeviceNumberAndGplotId(deviceNumberAndIp.deviceNumber,gplotId);
            //add all fv filters to cache
            CommonFvFilterUtil.addOrUpdateFvFilter(deviceNumberAndIp.deviceNumber,fvDimensionFilters,
                    sb.append("device_name-").append(deviceNumberAndIp.deviceNumber).
                    append(" gplot_id-").append(gplotId).toString());
            //load all opt dimension rule from fv_dimension table by device_number + gpolt_id
            List<OptFilter> optFilters = iDeviceService.loadAllOptFiterByDeviceNumberAndGplotId(deviceNumberAndIp.deviceNumber,gplotId);
            //add all opt filters to cache
            CommonOptFilterUtil.addOrUpdateAnalyzer(deviceNumberAndIp.deviceNumber , optFilters , "xxx");
            //更新DEVICE_NUMBER和DEVICE_IP之间的对应关系
            CommonCacheUtil.addOrUpdateDeviceNumberAndIp(deviceNumberAndIp.deviceNumber , deviceNumberAndIp.deviceIp);
        }
    }

}
