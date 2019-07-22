package com.zjucsc.application.system.service.hessian_impl;

import com.zjucsc.application.config.Common;
import com.zjucsc.application.domain.bean.*;
import com.zjucsc.application.system.mapper.base.BaseServiceImpl;
import com.zjucsc.application.system.service.hessian_iservice.IDeviceService;
import com.zjucsc.application.system.service.hessian_iservice.IGplotService;
import com.zjucsc.application.system.service.hessian_iservice.IWhiteProtocolService;
import com.zjucsc.application.system.service.hessian_mapper.GplotMapper;
import com.zjucsc.application.util.CommonCacheUtil;
import com.zjucsc.application.util.CommonFvFilterUtil;
import com.zjucsc.application.util.CommonOptFilterUtil;
import com.zjucsc.common.exceptions.ProtocolIdNotValidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hongqianhui
 */
@Service
@Slf4j
public class GplotServiceImpl extends BaseServiceImpl<Gplot,GplotMapper> implements IGplotService {

    @Autowired private IDeviceService iDeviceService;
    @Autowired private IWhiteProtocolService iWhiteProtocolService;

    @Override
    public BaseResponse addNewGplot(Gplot gplot) {
        this.baseMapper.insertById(gplot);
        return BaseResponse.OK();
    }

    @Override
    public BaseResponse changeGplot(int gplotId) throws ProtocolIdNotValidException {
        StringBuilder sb = new StringBuilder();
        //移除旧组态图上的所有规则
        CommonOptFilterUtil.removeAllOptFilter();
        CommonFvFilterUtil.removeAllFvFilter();
        //移除旧组态图上的所有DEVICE_NUMBER和DEVICE_IP之间的对应关系
        //同时删除该组态图对应的所有DEVICE_NUMBER保存的StatisticInfoSaveBean【设备upload、download等报文信息】
        CommonCacheUtil.removeAllCachedDeviceNumber();
        CommonCacheUtil.removeAllDeviceNumberToName();
        //CommonCacheUtil.removeAllDevices();
        //reload filters
        List<Device> deviceNumbers = iDeviceService.selectByGplotId(gplotId);   //load all device
        if (log.isInfoEnabled()) {// from device_info table by gplot_id
            log.info("***************\n切换组态图，加载新组态图下所有设备，新设备：{} ***************", deviceNumbers);
        }
        for (Device device : deviceNumbers) {
            sb.delete(0 , sb.length());
            //load all fv dimension rule from fv_dimension table by device_number + gplot_id
            List<Rule> rules = iDeviceService.
                    loadAllFvDimensionFilterByDeviceNumberAndGplotId(device.getDeviceNumber(),gplotId);
            //add all fv filters to cache
            if (rules.size() > 0) {
                CommonFvFilterUtil.addOrUpdateFvFilter(device.getDeviceIp(), rules,
                        sb.append("device_name-").append(device.getDeviceNumber()).
                                append(" gplot_id-").append(gplotId).toString());
            }
            //load all opt dimension rule from opt filter table by device_number + gplot_id
            List<OptFilterForFront> optFilters = iDeviceService.loadAllOptFilterByDeviceNumberAndGplotId(device.getDeviceNumber(),gplotId);
            //add all opt filters to cache
            if (optFilters.size() > 0) {
                for (OptFilterForFront optFilter : optFilters) {
                    CommonOptFilterUtil.addOrUpdateAnalyzer(device.getDeviceIp(), optFilter, optFilter.toString());
                }
            }
            //更新DEVICE_NUMBER和DEVICE_IP之间的对应关系
            //更新DEVICE_NUMBER和StatisticInfoSaveBean【设备upload、download等报文信息】
            CommonCacheUtil.addOrUpdateDeviceNumberAndTAG(device.getDeviceNumber() , device.getDeviceIp());
            CommonCacheUtil.addDeviceNumberToName(device.getDeviceNumber(),device.getDeviceInfo());
            CommonCacheUtil.addOrUpdateDeviceManually(device);
            /*************************
             * INIT WHITE PROTOCOL
             *************************/
            DeviceProtocol deviceProtocol = iWhiteProtocolService.selectByDeviceNumber(device.getDevice_id());
            CommonCacheUtil.addWhiteProtocolToCache(deviceProtocol.getDeviceNumber(),deviceProtocol.getProtocolName());
        }
        //更新缓存中的组态图ID
        Common.GPLOT_ID = gplotId;

        return BaseResponse.OK();
    }

    @Override
    public List<Gplot> selectAll() {
        return this.baseMapper.selectAll();
    }

}