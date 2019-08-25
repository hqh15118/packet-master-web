package com.zjucsc.application.system.service.hessian_impl;

import com.zjucsc.application.system.service.hessian_iservice.IArtOptCommandService;
import com.zjucsc.application.system.service.hessian_mapper.ArtOptCommandMapper;
import com.zjucsc.application.util.ArtOptAttackUtil;
import com.zjucsc.attack.s7comm.S7OptCommandConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtOptCommandServiceImpl implements IArtOptCommandService {

    @Autowired private ArtOptCommandMapper artOptCommandMapper;

    @Override
    public void insertArtOptCommand(S7OptCommandConfig s7OptCommandConfig) {
        artOptCommandMapper.insertArtOptCommand(s7OptCommandConfig);
        resetConfigs(s7OptCommandConfig.getProtocol());
    }

    @Override
    public void updateArtOptCommand(S7OptCommandConfig s7OptCommandConfig) {
        artOptCommandMapper.updateArtOptCommand(s7OptCommandConfig);
        resetConfigs(s7OptCommandConfig.getProtocol());
    }

    @Override
    public S7OptCommandConfig deleteArtOptCommand(int id) {
        S7OptCommandConfig s7OptCommandConfig = artOptCommandMapper.deleteArtOptCommand(id);
        resetConfigs(s7OptCommandConfig.getProtocol());
        return s7OptCommandConfig;
    }

    @Override
    public S7OptCommandConfig selectArtOptCommand(String deviceMac, String opName) {
        return artOptCommandMapper.selectArtOptCommand(deviceMac, opName);
    }

    @Override
    public List<S7OptCommandConfig> selectBatch() {
        return artOptCommandMapper.selectBatch();
    }

    private void resetConfigs(String protocol){
        List<S7OptCommandConfig> s7OptNameList = artOptCommandMapper.selectBatch();
        ArtOptAttackUtil.resetProtocol2OptCommandConfig(protocol,s7OptNameList);
    }
}
