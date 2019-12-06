package com.zjucsc.application.system.service.hessian_mapper;

import com.zjucsc.application.domain.bean.ArtOpNameConfigStateDTO;
import com.zjucsc.attack.bean.BaseOpName;

import java.util.List;

public interface ArtOptNameMapper {
    void insertArtOptName(String optNameConfig);
    void updateArtOptName(String optNameConfig);
    BaseOpName deleteArtOptName(int id , String protocol);
    BaseOpName selectArtOptName(int id , String protocol);
    List<BaseOpName> selectBatch();
    List<BaseOpName> selectByProtocolPaged(int page , int limit , String protocol);
    //return opName
    String enableOptNameConfig(ArtOpNameConfigStateDTO artOpNameConfigStateDTO);
}
