package com.zjucsc.application.system.service.hessian_iservice;

import com.zjucsc.application.domain.bean.ArtOpNameConfigStateDTO;
import com.zjucsc.application.domain.bean.ArtOpNameDTO;
import com.zjucsc.attack.bean.BaseOpName;

import java.util.List;

public interface IArtOptNameService {
    void insertArtOptName(BaseOpName s7OptName);
    void updateArtOptName(BaseOpName s7OptName);
    BaseOpName deleteArtOptName(ArtOpNameDTO artOpNameDTO);
    BaseOpName selectArtOptName(ArtOpNameDTO artOpNameDTO);
    List<BaseOpName> selectBatch();

    List<BaseOpName> selectByProtocol(String protocol);

    boolean enableOptNameConfig(ArtOpNameConfigStateDTO artOpNameConfigStateDTO);
}
