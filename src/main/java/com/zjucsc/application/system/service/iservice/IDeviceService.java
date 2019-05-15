package com.zjucsc.application.system.service.iservice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjucsc.application.system.entity.Device;

/**
 * @author hongqianhui
 */
public interface IDeviceService extends IService<Device> {
    void updateDeviceInfo(Device device);
    Device selectDeviceByIdAndGplot(String deviceId,int plotId);
    String selectDeviceNumberByCollectorTag(String collectorId);
}
