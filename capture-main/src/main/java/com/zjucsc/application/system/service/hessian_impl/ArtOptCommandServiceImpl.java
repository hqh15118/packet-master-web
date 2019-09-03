package com.zjucsc.application.system.service.hessian_impl;

import com.zjucsc.application.domain.non_hessian.CommandState;
import com.zjucsc.application.system.service.hessian_iservice.IArtOptCommandService;
import com.zjucsc.application.system.service.hessian_mapper.ArtOptCommandMapper;
import com.zjucsc.attack.util.ArtOptAttackUtil;
import com.zjucsc.attack.config.S7OptCommandConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtOptCommandServiceImpl implements IArtOptCommandService {

    @Autowired private ArtOptCommandMapper artOptCommandMapper;

    @Override
    public void insertArtOptCommand(S7OptCommandConfig s7OptCommandConfig) {
        artOptCommandMapper.insertArtOptCommand(s7OptCommandConfig);
        resetConfigs();
    }

    @Override
    public void updateArtOptCommand(S7OptCommandConfig s7OptCommandConfig) {
        artOptCommandMapper.updateArtOptCommand(s7OptCommandConfig);
        resetConfigs();
    }

    @Override
    public S7OptCommandConfig deleteArtOptCommand(int id) {
        S7OptCommandConfig s7OptCommandConfig = artOptCommandMapper.deleteArtOptCommand(id);
        resetConfigs();
        return s7OptCommandConfig;
    }

    @Override
    public S7OptCommandConfig selectArtOptCommand(String opName) {
        return artOptCommandMapper.selectArtOptCommand( opName);
    }

    @Override
    public List<S7OptCommandConfig> selectBatch() {
        return artOptCommandMapper.selectBatch();
    }

    @Override
    public void changeCommandState(CommandState commandState) {
        String changeProtocol = artOptCommandMapper.changeState(commandState.getId(),commandState.isEnable());
        resetConfigs();
    }

    private void resetConfigs(){
        //需要加一个协议字段
        List<S7OptCommandConfig> s7OptNameList = artOptCommandMapper.selectBatch();
        ArtOptAttackUtil.resetProtocol2OptCommandConfig(s7OptNameList);
    }
}
