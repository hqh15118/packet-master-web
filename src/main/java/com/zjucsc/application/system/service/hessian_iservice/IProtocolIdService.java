package com.zjucsc.application.system.service.hessian_iservice;

import com.zjucsc.application.domain.bean.ProtocolId;
import com.zjucsc.application.system.mapper.base.IService;

import java.util.List;

/**
 * @author hongqianhui
 */
public interface IProtocolIdService extends IService<ProtocolId> {
    int getMax();

    void saveOrUpdateBatch(List<ProtocolId> protocol);

    List<ProtocolId> selectAll();

    void deleteByProtocolId(int protocolId);
}
