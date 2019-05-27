package com.zjucsc.application.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjucsc.application.domain.bean.ArtConfigPaged;
import com.zjucsc.application.system.entity.ArtConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hongqianhui
 */
public interface ArtConfigMapper extends BaseMapper<ArtConfig> {
    void deleteArtConfigByProtocolIdAndMinLength(@Param("protocolId")int protocolId , @Param("minLength")int minLength);
    List<ArtConfig> getAllArtConfig();
    List<ArtConfig> getConfigPaged(@Param("protocolId")int protocolId,@Param("minLength")int minLength
    ,@Param("start")int start , @Param("end")int end);
}
