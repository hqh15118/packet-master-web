package com.zjucsc.application.system.service.hessian_impl;

import com.zjucsc.application.system.entity.ProtocolId;
import com.zjucsc.application.system.mapper.base.BaseServiceImpl;
import com.zjucsc.application.system.service.hessian_iservice.IProtocolIdService;
import com.zjucsc.application.system.service.hessian_mapper.ProtocolIdMapper;
import org.springframework.stereotype.Service;

/**
 * @author hongqianhui
 */
@Service
public class ProtocolIdServiceImpl extends BaseServiceImpl<ProtocolIdMapper, ProtocolId> implements IProtocolIdService {

    @Override
    public int getMax() {
        return this.baseMapper.getMax();
    }
}
