package com.zjucsc.application.system.service.hessian_iservice;

import com.zjucsc.application.domain.non_hessian.CommandState;
import com.zjucsc.attack.config.S7OptCommandConfig;

import java.util.List;

public interface IArtOptCommandService {
    void insertArtOptCommand(S7OptCommandConfig s7OptCommandConfig);
    void updateArtOptCommand(S7OptCommandConfig s7OptCommandConfig);
    S7OptCommandConfig deleteArtOptCommand(int id);
    S7OptCommandConfig selectArtOptCommand(String opName);
    List<S7OptCommandConfig> selectBatch();

    void changeCommandState(CommandState commandState);
}
