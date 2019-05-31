package com.zjucsc.application.system.service.hessian_mapper;

import com.zjucsc.application.domain.bean.FvDimensionFilter;
import com.zjucsc.application.system.mapper.base.BaseMapper;

import java.util.List;

/**
 * @author hongqianhui
 */
public interface FvDimensionFilterMapper extends BaseMapper<FvDimensionFilter> {
    List<FvDimensionFilter> selectByDeviceId(String deviceId);

    void deleteAllFilterByDeviceNumberAndGplotId(String deviceNumber,  int gplotId);

    void saveOrUpdateBatch(List<FvDimensionFilter> fvDimensionFilterList);
}
