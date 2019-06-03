package com.zjucsc.application.system.service.hessian_mapper;

import com.zjucsc.application.domain.bean.ArtConfig;
import com.zjucsc.application.system.mapper.base.BaseMapper;

import java.util.List;
import java.util.Set;

/**
 * @author hongqianhui
 */
public interface ArtConfigMapper extends BaseMapper<ArtConfig> {
    void deleteArtConfigByProtocolIdAndMinLength(int protocolId, int minLength);
    List<ArtConfig> getConfigPaged(int protocolId, int minLength
            , int start, int end);
    List<ArtConfig> selectAllConfig();

    /**
     * 筛选出所有标记显示的工艺参数
     * @return
     */
    List<String> selectAllShowArt();
}
