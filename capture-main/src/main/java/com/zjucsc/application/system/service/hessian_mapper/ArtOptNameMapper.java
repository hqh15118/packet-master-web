package com.zjucsc.application.system.service.hessian_mapper;

import com.zjucsc.application.domain.bean.ArtOpNameConfigStateDTO;
import com.zjucsc.attack.bean.BaseOpName;

import java.util.List;

public interface ArtOptNameMapper {
    void insertArtOptName(BaseOpName optNameConfig);
    void updateArtOptName(BaseOpName optNameConfig);
    BaseOpName deleteArtOptName(int id , String protocol);
    BaseOpName selectArtOptName(int id , String protocol);
    List<BaseOpName> selectBatch();
    List<BaseOpName> selectByProtocol(String protocol);
    //return opName
    String enableOptNameConfig(ArtOpNameConfigStateDTO artOpNameConfigStateDTO);
}
