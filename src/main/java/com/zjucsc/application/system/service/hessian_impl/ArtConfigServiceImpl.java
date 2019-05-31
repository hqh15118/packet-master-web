package com.zjucsc.application.system.service.hessian_impl;

import com.zjucsc.application.domain.bean.ArtConfig;
import com.zjucsc.application.domain.bean.ArtConfigPaged;
import com.zjucsc.application.system.mapper.base.BaseServiceImpl;
import com.zjucsc.application.system.service.hessian_iservice.IArtConfigService;
import com.zjucsc.application.system.service.hessian_mapper.ArtConfigMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtConfigServiceImpl extends BaseServiceImpl<ArtConfigMapper, ArtConfig> implements IArtConfigService {
    @Override
    public void deleteByProtocolIdAndMinLength(int protocolId, int minLength) {
        this.baseMapper.deleteArtConfigByProtocolIdAndMinLength(protocolId, minLength);
    }

    @Override
    public List<ArtConfig> getConfigPaged(ArtConfigPaged artConfigPaged) {
        int page = artConfigPaged.getPage();
        int limit = artConfigPaged.getLimit();
        int startIndex = page * 20;
        return this.baseMapper.getConfigPaged(artConfigPaged.getProtocolId(),
                artConfigPaged.getMinLength(),startIndex,limit);
    }

    @Override
    public List<ArtConfig> selectAllConfig() {
        return this.baseMapper.selectAllConfig();
    }

}
