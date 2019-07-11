package com.zjucsc.application.system.service.hessian_mapper;


import java.util.List;

public interface OptAttackMapper {
    void deleteOptAttackConfigById(String protocol , int configId);
    List selectAllOptAttackConfigByProtocol(String protocol);
    void addOrUpdateAllOptAttackConfigByProtocolAndId(String protocol,int configId,String jsonData);
}
