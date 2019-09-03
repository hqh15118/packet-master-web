package com.zjucsc.application.system.service.hessian_mapper;

import com.zjucsc.attack.bean.BaseOpName;
import com.zjucsc.attack.s7comm.S7OpName;

import java.util.List;

public interface ArtOptNameMapper {
    void insertArtOptName(BaseOpName s7OptName);
    void updateArtOptName(BaseOpName s7OptName);
    BaseOpName deleteArtOptName(String opName);
    BaseOpName selectArtOptName(String opName);
    List<BaseOpName> selectBatch();
    List<BaseOpName> selectByProtocol(String protocol);
}
