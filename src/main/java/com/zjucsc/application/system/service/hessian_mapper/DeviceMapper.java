package com.zjucsc.application.system.service.hessian_mapper;
import com.zjucsc.application.domain.bean.DeviceNumberAndIp;
import com.zjucsc.application.system.entity.Device;
import com.zjucsc.application.system.entity.FvDimensionFilter;
import com.zjucsc.application.system.entity.OptFilter;
import com.zjucsc.application.system.mapper.base.BaseMapper;

import java.util.List;

/**
 * @author hongqianhui
 */
public interface DeviceMapper extends BaseMapper<Device> {
    void updateDeviceInfo(int deviceType, String deviceInfo,
                          String deviceIp, String deviceId,
                         int gplotId);

    Device selectDeviceByIdAndGplot(String deviceId, int gplotId);

    String selectDeviceNumberByCollectorTagAndGplotId( String collectorId, int gplotId);

    List<DeviceNumberAndIp> loadAllDevicesByGplotId(int gplotId);

    List<FvDimensionFilter> loadAllFvDimensionFilterByDeviceNumberAndGplotId( String deviceNumber, int gplotId);

    List<OptFilter> loadAllOptFilterByDeviceNumberAndGplotId( String deviceNumber,  int gplotId);

    void removeDeviceByDeviceNumberAndGplotId( String deviceNumber,  int gplotId);

    void removeAllDevicesByGplotId( int gplotId);
}
