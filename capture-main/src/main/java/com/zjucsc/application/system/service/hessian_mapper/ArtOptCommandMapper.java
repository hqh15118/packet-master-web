package com.zjucsc.application.system.service.hessian_mapper;

import com.zjucsc.attack.config.S7OptCommandConfig;

import java.util.List;

public interface ArtOptCommandMapper {
    void insertArtOptCommand(S7OptCommandConfig s7OptCommandConfig);
    void updateArtOptCommand(S7OptCommandConfig s7OptCommandConfig);
    S7OptCommandConfig deleteArtOptCommand(int id);
    S7OptCommandConfig selectArtOptCommand(String opName);
    List<S7OptCommandConfig> selectBatch();
    String changeState(int id , boolean enable);
}
