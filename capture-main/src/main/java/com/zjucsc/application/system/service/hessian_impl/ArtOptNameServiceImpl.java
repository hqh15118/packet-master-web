package com.zjucsc.application.system.service.hessian_impl;

import com.zjucsc.application.system.service.hessian_iservice.IArtOptNameService;
import com.zjucsc.application.system.service.hessian_mapper.ArtOptNameMapper;
import com.zjucsc.attack.util.ArtOptAttackUtil;
import com.zjucsc.attack.s7comm.S7OptName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ArtOptNameServiceImpl implements IArtOptNameService {

    @Autowired private ArtOptNameMapper optNameMapper;

    @Override
    public void insertArtOptName(S7OptName s7OptName) {
        optNameMapper.insertArtOptName(s7OptName);
        resetConfigs();
    }

    @Override
    public void updateArtOptName(S7OptName s7OptName) {
        optNameMapper.updateArtOptName(s7OptName);
        resetConfigs();
    }

    @Override
    public S7OptName deleteArtOptName(String opName) {
        S7OptName s7OptName = optNameMapper.deleteArtOptName(opName);
        resetConfigs();
        return s7OptName;
    }

    @Override
    public S7OptName selectArtOptName(String opName) {
        return optNameMapper.selectArtOptName(opName);
    }

    @Override
    public List<S7OptName> selectBatch() {
        return optNameMapper.selectBatch();
    }

    private void resetConfigs(){
        List<S7OptName> s7OptNameList = optNameMapper.selectBatch();
        ArtOptAttackUtil.resetOpName2OptConfig(s7OptNameList);
    }
}
