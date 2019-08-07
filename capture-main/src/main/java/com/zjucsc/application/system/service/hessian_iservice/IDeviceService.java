package com.zjucsc.application.system.service.hessian_iservice;
import com.zjucsc.application.domain.bean.*;

import java.util.List;
import java.util.Map;

/**
 * @author hongqianhui
 */
public interface IDeviceService {
    Device selectDeviceByIdAndGplot(String deviceId, int plotId);
    String selectDeviceNumberByCollectorTag(String collectorId, int gplotId);
    List<DeviceNumberAndIp> loadAllDevicesByGplotId(int gplotId);
    List<Rule> loadAllFvDimensionFilterByDeviceNumberAndGplotId(String deviceNumber, int gplotId);
    List<OptFilterForFront> loadAllOptFilterByDeviceNumberAndGplotId(String deviceNumber, int gplotId);
    void removeDeviceByDeviceNumberAndGplotId(String deviceNumber, int gplotId);
    void removeAllDevicesByGplotId(int gplotId);
    void saveBatch(List<Device> devices);

    List<Device> selectByGplotId(int gplotId);

    StatisticInfo selectHistoryDeviceRunInfo(String deviceId, String startTime, String endTime, String intervalType);

    void saveStatisticInfo(Map<String, StatisticInfoSaveBean> map);

    void saveOrUpdateDevice(Device device);

    Device removeDevice(String deviceNumber);

    void changeDeviceConfigState(String deviceNumber , boolean isConfig);

    List<Device> selectAllConfiguredDevices();

    Device selectDeviceByDeviceNumber(String deviceNumber);
}
