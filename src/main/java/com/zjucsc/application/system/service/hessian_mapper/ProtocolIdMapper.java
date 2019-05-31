package com.zjucsc.application.system.service.hessian_mapper;

import com.zjucsc.application.domain.bean.ProtocolId;
import com.zjucsc.application.system.mapper.base.BaseMapper;

import java.util.List;

/**
 * @author hongqianhui
 */
public interface ProtocolIdMapper extends BaseMapper<ProtocolId> {

    int getMax();

    void saveOrUpdateBatch(List<ProtocolId> protocol);

    List<ProtocolId> selectAll();

    void deleteByProtocolId(int protocolId);
}
