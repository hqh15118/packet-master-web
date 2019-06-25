package com.zjucsc.application.system.service.hessian_mapper;

import com.zjucsc.application.domain.bean.Protocol;
import com.zjucsc.application.system.mapper.base.BaseMapper;

import java.util.List;

/**
 * @author hongqianhui
 */
public interface ProtocolIdMapper extends BaseMapper<Protocol> {

    int getMax();

    void saveOrUpdateBatch(List<Protocol> protocol);

    List<Protocol> selectAll();

    void deleteByProtocolId(int protocolId);
}
