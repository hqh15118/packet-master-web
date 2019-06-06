package com.zjucsc.application.system.service.hessian_iservice;

import com.zjucsc.application.domain.bean.Protocol;
import com.zjucsc.application.system.mapper.base.IService;

import java.util.List;

/**
 * @author hongqianhui
 */
public interface IProtocolIdService extends IService<Protocol> {

    void saveOrUpdateBatch(List<Protocol> protocol);

    List<Protocol> selectAll();

    void deleteByProtocolId(int protocolId);
}
