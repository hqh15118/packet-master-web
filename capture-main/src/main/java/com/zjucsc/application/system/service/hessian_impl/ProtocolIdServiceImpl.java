package com.zjucsc.application.system.service.hessian_impl;

import com.zjucsc.application.domain.bean.Protocol;
import com.zjucsc.application.system.mapper.base.BaseServiceImpl;
import com.zjucsc.application.system.service.hessian_iservice.IProtocolIdService;
import com.zjucsc.application.system.service.hessian_mapper.ProtocolIdMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hongqianhui
 */
@Service
public class ProtocolIdServiceImpl extends BaseServiceImpl<Protocol,ProtocolIdMapper> implements IProtocolIdService {

    @Override
    public void saveOrUpdateBatch(List<Protocol> protocol) {
        this.baseMapper.saveOrUpdateBatch(protocol);
    }

    @Override
    public List<Protocol> selectAll() {
        return this.baseMapper.selectAll();
    }

    @Override
    public void deleteByProtocolId(int protocolId) {
        this.baseMapper.deleteByProtocolId(protocolId);
    }
}
