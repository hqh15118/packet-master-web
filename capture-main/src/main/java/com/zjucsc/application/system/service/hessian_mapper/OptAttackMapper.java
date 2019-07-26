package com.zjucsc.application.system.service.hessian_mapper;


import com.zjucsc.attack.bean.BaseOptConfig;

import java.util.List;

public interface OptAttackMapper {
    BaseOptConfig deleteOptAttackConfigById(int protocolId , int configId);
    List<BaseOptConfig> selectAllOptAttackConfigByProtocol(int protocolId ,int page,int limit);
    void addOrUpdateAllOptAttackConfigByProtocolAndId(int protocolId,int configId,String jsonData);
    BaseOptConfig selectOptConfigByProtocolAndId(int protocolId,int id);
}
