package com.zjucsc.application.system.service.hessian_mapper;

import com.zjucsc.attack.s7comm.S7OptName;

import java.util.List;

public interface ArtOptNameMapper {
    void insertArtOptName(S7OptName s7OptName);
    void updateArtOptName(S7OptName s7OptName);
    S7OptName deleteArtOptName(String opName);
    S7OptName selectArtOptName(String opName);
    List<S7OptName> selectBatch();
}
