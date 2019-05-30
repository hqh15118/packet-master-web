package com.zjucsc.application.system.service.hessian_mapper;

import com.zjucsc.application.system.entity.ArtConfig;
import com.zjucsc.application.system.mapper.base.BaseMapper;

import java.util.List;

/**
 * @author hongqianhui
 */
public interface ArtConfigMapper extends BaseMapper<ArtConfig> {
    void deleteArtConfigByProtocolIdAndMinLength(int protocolId, int minLength);
    List<ArtConfig> getConfigPaged(int protocolId, int minLength
            , int start, int end);
}
