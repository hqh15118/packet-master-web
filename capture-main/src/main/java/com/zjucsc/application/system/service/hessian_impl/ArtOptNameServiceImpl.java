package com.zjucsc.application.system.service.hessian_impl;

import com.zjucsc.application.domain.bean.ArtOpNameConfigStateDTO;
import com.zjucsc.application.domain.bean.ArtOpNameDTO;
import com.zjucsc.application.system.service.hessian_iservice.IArtOptNameService;
import com.zjucsc.application.system.service.hessian_mapper.ArtOptNameMapper;
import com.zjucsc.attack.bean.BaseOpName;
import com.zjucsc.attack.util.ArtOptAttackUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ArtOptNameServiceImpl implements IArtOptNameService {

    @Autowired private ArtOptNameMapper optNameMapper;

    @Override
    public void insertArtOptName(String optName2OpConfig) {
        optNameMapper.insertArtOptName(optName2OpConfig);
        resetConfigs();
    }

    @Override
    public void updateArtOptName(String optName2OpConfig) {
        optNameMapper.updateArtOptName(optName2OpConfig);
        resetConfigs();
    }

    @Override
    public BaseOpName deleteArtOptName(ArtOpNameDTO artOpNameDTO) {
        BaseOpName baseOpName = optNameMapper.deleteArtOptName(artOpNameDTO.getId(),artOpNameDTO.getProtocol());
        resetConfigs();
        return baseOpName;
    }

    @Override
    public BaseOpName selectArtOptName(ArtOpNameDTO artOpNameDTO) {
        return optNameMapper.selectArtOptName(artOpNameDTO.getId(),artOpNameDTO.getProtocol());
    }

    @Override
    public List<BaseOpName> selectBatch() {
        return optNameMapper.selectBatch();
    }

    @Override
    public List<BaseOpName> selectByProtocolPaged(int page , int limit ,String protocol) {
        return optNameMapper.selectByProtocolPaged(page, limit, protocol);
    }

    @Override
    public boolean enableOptNameConfig(ArtOpNameConfigStateDTO artOpNameConfigStateDTO) {
        optNameMapper.enableOptNameConfig(artOpNameConfigStateDTO); //修改数据库状态
        //String opName = optNameMapper.enableOptNameConfig(artOpNameConfigStateDTO);
        //ArtOptAttackUtil.changeOpName2OptConfigState(opName,artOpNameConfigStateDTO.isEnable());
        resetConfigs();
        return true;
    }

    private void resetConfigs(){
        List<BaseOpName> optNameList = optNameMapper.selectBatch();
        ArtOptAttackUtil.resetOpName2OptConfig(optNameList);
    }
}
