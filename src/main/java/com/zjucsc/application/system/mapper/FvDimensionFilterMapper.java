package com.zjucsc.application.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjucsc.application.system.entity.FvDimensionFilter;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hongqianhui
 */
public interface FvDimensionFilterMapper extends BaseMapper<FvDimensionFilter> {
    List<FvDimensionFilter> selectByDeviceId(@Param("device_id") int deviceId);
}
