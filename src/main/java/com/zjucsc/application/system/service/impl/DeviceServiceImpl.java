package com.zjucsc.application.system.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.domain.bean.DeviceNumberAndIp;
import com.zjucsc.application.system.entity.Device;
import com.zjucsc.application.system.entity.FvDimensionFilter;
import com.zjucsc.application.system.entity.OptFilter;
import com.zjucsc.application.system.mapper.DeviceMapper;
import com.zjucsc.application.system.service.iservice.IDeviceService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hongqianhui
 */
@Service("deviceservice")
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements IDeviceService {

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
    public List<FvDimensionFilter> loadAllFvDimensionFilterByDeviceNumberAndGplotId(String deviceNumber, int gplotId) {
        return this.baseMapper.loadAllFvDimensionFilterByDeviceNumberAndGpotId(deviceNumber,gplotId);
    }

    @Override
    public List<OptFilter> loadAllOptFiterByDeviceNumberAndGplotId(String deviceNumber, int gplotId) {
        return this.baseMapper.loadAllOptFiterByDeviceNumberAndGplotId(deviceNumber , gplotId);
    }

    @Override
    public void removeDeviceByDeviceNumberAndGplotId(String deviceNumber, int gplotId) {
        this.baseMapper.removeDeviceByDeviceNumberAndGplotId(deviceNumber,gplotId);
    }

    @Override
    public void removeAllDevicesByGplotId(int gplotId) {
        this.baseMapper.removeAllDevicesByGplotId(gplotId);
    }
}
