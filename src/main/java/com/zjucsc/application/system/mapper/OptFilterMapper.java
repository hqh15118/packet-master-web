package com.zjucsc.application.system.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjucsc.application.system.entity.OptFilter;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hongqianhui
 */
public interface OptFilterMapper extends BaseMapper<OptFilter> {

    List<Integer> selectTargetOptFilter(@Param("device_id") String device, @Param("type") int type,
                                          @Param("protocolId") int protocolId);
}