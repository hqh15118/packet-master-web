package com.zjucsc.application.system.service.hessian_impl;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.domain.bean.*;
import com.zjucsc.application.system.mapper.base.BaseServiceImpl;
import com.zjucsc.application.system.service.hessian_iservice.IDeviceService;
import com.zjucsc.application.system.service.hessian_mapper.DeviceMapper;
import com.zjucsc.application.util.CommonCacheUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author hongqianhui
 */
@Service
public class DeviceServiceImpl extends BaseServiceImpl< Device , DeviceMapper> implements IDeviceService {

    @Override
    public void updateDeviceInfo(Device device) {
        this.baseMapper.updateDeviceInfo(device.getDeviceType(),
                                        device.getDeviceInfo(),
                                        device.getDeviceTag(),
                                        device.getDeviceNumber(),
                                        device.getGPlotId());
    }

    @Override
    public Device selectDeviceByIdAndGplot(String deviceId, int plotId) {
        return this.baseMapper.selectDeviceByIdAndGplot(deviceId,plotId);
    }

    @Override
    public String selectDeviceNumberByCollectorTag(String collectorId , int gplotId) {
        return this.baseMapper.selectDeviceNumberByCollectorTagAndGplotId(collectorId , gplotId);
    }

    @Override
    public List<DeviceNumberAndIp> loadAllDevicesByGplotId(int gplotId) {
        return this.baseMapper.loadAllDevicesByGplotId(gplotId);
    }

    @Override
    public List<Rule> loadAllFvDimensionFilterByDeviceNumberAndGplotId(String deviceNumber, int gplotId) {
        return this.baseMapper.loadAllFvDimensionFilterByDeviceNumberAndGplotId(deviceNumber,gplotId);
    }

    @Override
    public List<OptFilterForFront> loadAllOptFilterByDeviceNumberAndGplotId(String deviceNumber, int gplotId) {
        return this.baseMapper.loadAllOptFilterByDeviceNumberAndGplotId(deviceNumber , gplotId);
    }

    @Override
    public void removeDeviceByDeviceNumberAndGplotId(String deviceNumber, int gplotId) {
        this.baseMapper.removeDeviceByDeviceNumberAndGplotId(deviceNumber,gplotId);
    }

    @Override
    public void removeAllDevicesByGplotId(int gplotId) {
        this.baseMapper.removeAllDevicesByGplotId(gplotId);
    }

    @Override
    public void saveBatch(List<Device> devices) {
        this.baseMapper.saveBatch(devices);
    }

    @Override
    public List<Device> selectByGplotId(int gplotId) {
        return this.baseMapper.selectByGplotId(gplotId);
    }

    @Override
    public StatisticInfo selectHistoryDeviceRunInfo(String deviceId, String startTime, String endTime, String intervalType) {
        return this.baseMapper.selectHistoryDeviceRunInfo(deviceId, startTime, endTime, intervalType,Common.GPLOT_ID);
    }

    @Override
    public void saveStatisticInfo(Map<String,StatisticInfoSaveBean> map) {
        this.baseMapper.saveStatisticInfo(map, Common.GPLOT_ID);
    }

    @Async
    @Override
    public void saveOrUpdateDevice(Device device) {
        this.baseMapper.saveOrUpdateDevice(device);
        CommonCacheUtil.addOrUpdateDeviceNumberAndTAG(device.getDeviceNumber(), device.getDeviceTag());
        CommonCacheUtil.addDeviceNumberToName(device.getDeviceNumber(),device.getDeviceInfo());
        CommonCacheUtil.addOrUpdateDeviceManually(device);
    }

    @Override
    public Device removeDevice(String deviceNumber) {
        return this.baseMapper.removeDeviceByDeviceNumber(deviceNumber);
    }

    @Override
    public void changeDeviceConfigState(String deviceNumber , boolean isConfig) {
        this.baseMapper.changeDeviceConfigState(deviceNumber , isConfig);
    }

    @Override
    public List<Device> selectAllConfiguredDevices() {
        return this.baseMapper.selectAllConfiguredDevices();
    }

    @Override
    public Device selectDeviceByDeviceNumber(String deviceNumber) {
        return this.baseMapper.selectDeviceByDeviceNumber(deviceNumber);
    }

}
