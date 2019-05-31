package com.zjucsc.application.system.service.hessian_impl;

import com.zjucsc.application.domain.bean.ProtocolId;
import com.zjucsc.application.system.mapper.base.BaseServiceImpl;
import com.zjucsc.application.system.service.hessian_iservice.IProtocolIdService;
import com.zjucsc.application.system.service.hessian_mapper.ProtocolIdMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hongqianhui
 */
@Service
public class ProtocolIdServiceImpl extends BaseServiceImpl<ProtocolIdMapper, ProtocolId> implements IProtocolIdService {

    @Override
    public int getMax() {
        return this.baseMapper.getMax();
    }

    @Override
    public void saveOrUpdateBatch(List<ProtocolId> protocol) {
        this.baseMapper.saveOrUpdateBatch(protocol);
    }

    @Override
    public List<ProtocolId> selectAll() {
        return this.baseMapper.selectAll();
    }

    @Override
    public void deleteByProtocolId(int protocolId) {
        this.baseMapper.deleteByProtocolId(protocolId);
    }
}
