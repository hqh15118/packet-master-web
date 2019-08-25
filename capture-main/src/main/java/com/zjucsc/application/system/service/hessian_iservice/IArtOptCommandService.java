package com.zjucsc.application.system.service.hessian_iservice;

import com.zjucsc.attack.s7comm.S7OptCommandConfig;

import java.util.List;

public interface IArtOptCommandService {
    void insertArtOptCommand(S7OptCommandConfig s7OptCommandConfig);
    void updateArtOptCommand(S7OptCommandConfig s7OptCommandConfig);
    S7OptCommandConfig deleteArtOptCommand(int id);
    S7OptCommandConfig selectArtOptCommand(String deviceMac,String opName);
    List<S7OptCommandConfig> selectBatch();
}
