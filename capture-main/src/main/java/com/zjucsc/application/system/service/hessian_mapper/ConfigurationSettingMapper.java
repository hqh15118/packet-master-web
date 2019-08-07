package com.zjucsc.application.system.service.hessian_mapper;

import com.zjucsc.application.domain.bean.ConfigurationSetting;
import com.zjucsc.application.system.mapper.base.BaseMapper;

import java.util.List;

/**
 * @author hongqianhui
 */
public interface ConfigurationSettingMapper extends BaseMapper<ConfigurationSetting> {
    List<ConfigurationSetting> selectPageInfo(String codeDes, int startIndex,
                                              int endIndex, int protocolId);
    int selectLikeCount(String codeDes, int protocolId);

    void updateFuncode(int protocol_id, String fun_code, String opt);

    List<ConfigurationSetting> selectAll();

    void saveOrUpdateBatch(List<ConfigurationSetting> configurationSettings);

    void deleteByProtocolIdAndFuncode(int protocolId, String funCode);

    void deleteByProtocolId(int protocolId);
}
