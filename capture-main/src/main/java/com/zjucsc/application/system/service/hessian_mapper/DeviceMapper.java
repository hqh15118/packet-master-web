package com.zjucsc.application.system.service.hessian_mapper;

import com.zjucsc.application.domain.bean.*;
import com.zjucsc.application.system.mapper.base.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * @author hongqianhui
 */
public interface DeviceMapper extends BaseMapper<Device> {
    void updateDeviceInfo(int deviceType, String deviceInfo,
                          String deviceIp, String deviceId,
                          int gplotId);

    Device selectDeviceByIdAndGplot(String deviceId, int gplotId);

    String selectDeviceNumberByCollectorTagAndGplotId(String collectorId, int gplotId);

    List<DeviceNumberAndIp> loadAllDevicesByGplotId(int gplotId);

    List<Rule> loadAllFvDimensionFilterByDeviceNumberAndGplotId(String deviceNumber, int gplotId);

    List<OptFilterForFront> loadAllOptFilterByDeviceNumberAndGplotId(String deviceNumber, int gplotId);

    void removeDeviceByDeviceNumberAndGplotId(String deviceNumber, int gplotId);

    void removeAllDevicesByGplotId(int gplotId);

    void saveBatch(List<Device> devices);

    List<Device> selectByGplotId(int gplotId);

    /**
     * @param deviceId
     * @param startTime
     * @param endTime
     * @param intervalType
     * @return
     */
    StatisticInfo selectHistoryDeviceRunInfo(String deviceId, String startTime, String endTime, String intervalType,
                                             int gplotId);

    void saveStatisticInfo(Map<String, StatisticInfoSaveBean> map, int gplotId);
}
