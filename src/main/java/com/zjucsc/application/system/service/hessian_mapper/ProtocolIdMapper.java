package com.zjucsc.application.system.service.hessian_mapper;

import com.zjucsc.application.system.entity.ProtocolId;
import com.zjucsc.application.system.mapper.base.BaseMapper;

/**
 * @author hongqianhui
 */
public interface ProtocolIdMapper extends BaseMapper<ProtocolId> {
    int getMax();
}
