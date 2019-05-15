package com.zjucsc.application.system.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjucsc.application.system.entity.Device;
import com.zjucsc.application.system.mapper.DeviceMapper;
import com.zjucsc.application.system.service.iservice.IDeviceService;
import org.springframework.stereotype.Service;

/**
 * @author hongqianhui
 */
@Service("deviceservice")
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements IDeviceService {

    @Override
    public void updateDeviceInfo(Device device) {
        this.baseMapper.updateDeviceInfo(device.getDeviceType(),
                                        device.getDeviceInfo(),
                                        device.getDeviceIp(),
                                        device.getDeviceNumber(),
                                        device.getGPlotId());
    }

    @Override
    public Device selectDeviceByIdAndGplot(String deviceId, int plotId) {
        return this.baseMapper.selectDeviceByIdAndGplot(deviceId,plotId);
    }

    @Override
    public String selectDeviceNumberByCollectorTag(String collectorId) {
        return this.baseMapper.selectDeviceNumberByCollectorTag(collectorId);
    }
}
