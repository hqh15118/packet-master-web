package com.zjucsc.application.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjucsc.application.domain.bean.ArtConfigPaged;
import com.zjucsc.application.system.entity.ArtConfig;
import com.zjucsc.application.system.mapper.ArtConfigMapper;
import com.zjucsc.application.system.service.iservice.IArtConfigService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hongqianhui
 */
@Service
public class ArtConfigServiceImpl extends ServiceImpl<ArtConfigMapper, ArtConfig> implements IArtConfigService {

    @Override
    public void deleteByProtocolIdAndMinLength(int protocolId, int minLength) {
        this.baseMapper.deleteArtConfigByProtocolIdAndMinLength(protocolId,minLength);
    }



    @Override
    public List<ArtConfig> getConfigPaged(ArtConfigPaged artConfigPaged) {
        int start = (artConfigPaged.getPage() - 1) * artConfigPaged.getLimit();
        int end = start + artConfigPaged.getLimit();
        return this.baseMapper.getConfigPaged(artConfigPaged.getProtocolId(),artConfigPaged.getMinLength(),
                start , end);
    }
}
