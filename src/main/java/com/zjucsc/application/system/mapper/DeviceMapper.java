package com.zjucsc.application.system.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjucsc.application.system.entity.Device;
import org.apache.ibatis.annotations.Param;

/**
 * @author hongqianhui
 */
public interface DeviceMapper extends BaseMapper<Device> {
    void updateDeviceInfo(@Param("deviceType")int deviceType , @Param("deviceInfo")String deviceInfo,
                          @Param("deviceIp")String deviceIp,@Param("deviceId")String deviceId,
                          @Param("gplotId")int gplotId);

    Device selectDeviceByIdAndGplot(@Param("deviceId")String deviceId , @Param("gplotId")int gplotId);

    String selectDeviceNumberByCollectorTag(@Param("collectorTag") String collectorId);
}
