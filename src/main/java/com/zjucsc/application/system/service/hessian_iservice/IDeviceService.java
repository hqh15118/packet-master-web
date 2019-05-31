package com.zjucsc.application.system.service.hessian_iservice;
import com.zjucsc.application.domain.bean.*;

import java.util.List;

/**
 * @author hongqianhui
 */
public interface IDeviceService {
    void updateDeviceInfo(Device device);
    Device selectDeviceByIdAndGplot(String deviceId, int plotId);
    String selectDeviceNumberByCollectorTag(String collectorId, int gplotId);
    List<DeviceNumberAndIp> loadAllDevicesByGplotId(int gplotId);
    List<FvDimensionFilter> loadAllFvDimensionFilterByDeviceNumberAndGplotId(String deviceNumber, int gplotId);
    List<OptFilter> loadAllOptFiterByDeviceNumberAndGplotId(String deviceNumber, int gplotId);
    void removeDeviceByDeviceNumberAndGplotId(String deviceNumber, int gplotId);
    void removeAllDevicesByGplotId(int gplotId);
    void saveBatch(List<Device> devices);

    Gplot selectByGplotId(int gplotId);
}
