package com.zjucsc.application.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjucsc.application.system.entity.ConfigurationSetting;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hongqianhui
 */
public interface ConfigurationSettingMapper extends BaseMapper<ConfigurationSetting> {
    List<ConfigurationSetting> selectPageInfo(@Param("codeDes") String codeDes , @Param("startIndex") int startIndex ,
                                       @Param("endIndex") int endIndex , @Param("protocolId") int protocolId);
    int selectLikeCount(@Param("codeDes") String codeDes , @Param("protocolId") int protocolId);

    void updateFuncode(@Param("protocol_id") int protocol_id , @Param("fun_code") int fun_code,@Param("opt")String opt);
}
