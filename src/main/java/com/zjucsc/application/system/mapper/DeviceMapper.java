package com.zjucsc.application.system.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjucsc.application.domain.bean.DeviceNumberAndIp;
import com.zjucsc.application.system.entity.Device;
import com.zjucsc.application.system.entity.FvDimensionFilter;
import com.zjucsc.application.system.entity.OptFilter;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @author hongqianhui
 */
public interface DeviceMapper extends BaseMapper<Device> {
    void updateDeviceInfo(@Param("deviceType")int deviceType , @Param("deviceInfo")String deviceInfo,
                          @Param("deviceIp")String deviceIp,@Param("deviceId")String deviceId,
                          @Param("gplotId")int gplotId);

    Device selectDeviceByIdAndGplot(@Param("deviceId")String deviceId , @Param("gplotId")int gplotId);

    String selectDeviceNumberByCollectorTagAndGplotId(@Param("collectorTag") String collectorId , @Param("gplotId")int gplotId);

    List<DeviceNumberAndIp> loadAllDevicesByGplotId(@Param("gplotId")int gplotId);

    List<FvDimensionFilter> loadAllFvDimensionFilterByDeviceNumberAndGplotId(@Param("deviceNumber") String deviceNumber, @Param("gplotId") int  gplotId);

    List<OptFilter> loadAllOptFilterByDeviceNumberAndGplotId(@Param("deviceNumber") String deviceNumber , @Param("gplotId") int gplotId);

    void removeDeviceByDeviceNumberAndGplotId(@Param("deviceNumber") String deviceNumber , @Param("gplotId") int gplotId);

    void removeAllDevicesByGplotId(@Param("gplotId")int gplotId);
}
