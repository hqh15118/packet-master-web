package com.zjucsc.application.system.service.iservice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjucsc.application.domain.bean.DeviceNumberAndIp;
import com.zjucsc.application.system.entity.Device;
import com.zjucsc.application.system.entity.FvDimensionFilter;
import com.zjucsc.application.system.entity.OptFilter;

import java.util.List;

/**
 * @author hongqianhui
 */
public interface IDeviceService extends IService<Device> {
    void updateDeviceInfo(Device device);
    Device selectDeviceByIdAndGplot(String deviceId,int plotId);
    String selectDeviceNumberByCollectorTag(String collectorId);
    List<DeviceNumberAndIp> loadAllDevicesByGplotId(int gplotId);
    List<FvDimensionFilter> loadAllFvDimensionFilterByDeviceNumberAndGpotId(String deviceNumber,int  gplotId);
    List<OptFilter> loadAllOptFiterByDeviceNumberAndGplotId(String deviceNumber , int gplotId);
    void removeDeviceByDeviceNumberAndGplotId(String deviceNumber , int gplotId);
    void removeAllDevicesByGplotId(int gplotId);
}
