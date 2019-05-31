package com.zjucsc.application.system.service.hessian_impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.domain.bean.DeviceNumberAndIp;
import com.zjucsc.application.domain.bean.Gplot;
import com.zjucsc.application.domain.exceptions.ProtocolIdNotValidException;
import com.zjucsc.application.system.entity.FvDimensionFilter;
import com.zjucsc.application.system.entity.OptFilter;
import com.zjucsc.application.system.mapper.base.BaseServiceImpl;
import com.zjucsc.application.system.service.hessian_iservice.IGplotService;
import com.zjucsc.application.system.service.hessian_mapper.GplotMapper;
import com.zjucsc.application.system.service.iservice.IDeviceService;
import com.zjucsc.application.util.CommonCacheUtil;
import com.zjucsc.application.util.CommonFvFilterUtil;
import com.zjucsc.application.util.CommonOptFilterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author hongqianhui
 */
@Service("gplotservice")
@Slf4j
public class GplotServiceImpl extends BaseServiceImpl<GplotMapper, Gplot> implements IGplotService {

    @Autowired private IDeviceService iDeviceService;

    @Transactional
    @Override
    public void addNewGplot(Gplot gplot) {
        this.baseMapper.insertById(gplot);
    }

    @Transactional
    @Async(value = "global_single_thread_executor")
    @Override
    public void changeGplot(int gplotId) throws ProtocolIdNotValidException {
        //更新缓存中的组态图ID
        Common.GPLOT_ID = gplotId;

        StringBuilder sb = new StringBuilder();
        //移除旧组态图上的所有规则
        CommonOptFilterUtil.removeAllOptFilter();
        CommonFvFilterUtil.removeAllFvFilter();
        //移除旧组态图上的所有DEVICE_NUMBER和DEVICE_IP之间的对应关系
        CommonCacheUtil.removeAllCachedDeviceNumber();

        //reload filters
        List<DeviceNumberAndIp> deviceNumbers = iDeviceService.loadAllDevicesByGplotId(gplotId);   //load all device from device_info table by gplot_id
        log.info("***************\n切换组态图，加载新组态图下所有设备，新设备：{} ***************" , deviceNumbers);
        for (DeviceNumberAndIp deviceNumberAndIp : deviceNumbers) {
            sb.delete(0 , sb.length());
            //load all fv dimension rule from fv_dimension table by device_number + gpolt_id
            List<FvDimensionFilter> fvDimensionFilters = iDeviceService.
                    loadAllFvDimensionFilterByDeviceNumberAndGplotId(deviceNumberAndIp.deviceNumber,gplotId);
            //add all fv filters to cache
            CommonFvFilterUtil.addOrUpdateFvFilter(deviceNumberAndIp.deviceIp,fvDimensionFilters,
                    sb.append("device_name-").append(deviceNumberAndIp.deviceNumber).
                    append(" g_plot_id-").append(gplotId).toString());
            //load all opt dimension rule from fv_dimension table by device_number + gpolt_id
            List<OptFilter> optFilters = iDeviceService.loadAllOptFiterByDeviceNumberAndGplotId(deviceNumberAndIp.deviceNumber,gplotId);
            //add all opt filters to cache
            CommonOptFilterUtil.addOrUpdateAnalyzer(deviceNumberAndIp.deviceIp , optFilters , "xxx");
            //更新DEVICE_NUMBER和DEVICE_IP之间的对应关系
            CommonCacheUtil.addOrUpdateDeviceNumberAndIp(deviceNumberAndIp.deviceNumber , deviceNumberAndIp.deviceIp);
        }
        log.info("***************\n切换组态图,从数据库中重新加载新该组态图下的所有规则，新规则为：\n 五元组规则：{} \n 功能码规则：{} \n ***************" , Common.FV_DIMENSION_FILTER_PRO,
                Common.OPERATION_FILTER_PRO);
    }

}
