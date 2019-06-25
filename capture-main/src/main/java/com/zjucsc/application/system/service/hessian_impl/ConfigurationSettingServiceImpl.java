package com.zjucsc.application.system.service.hessian_impl;


import com.zjucsc.application.domain.bean.ConfigurationForSelect;
import com.zjucsc.application.domain.bean.ConfigurationSetting;
import com.zjucsc.application.system.mapper.base.BaseServiceImpl;
import com.zjucsc.application.system.service.hessian_iservice.IConfigurationSettingService;
import com.zjucsc.application.system.service.hessian_mapper.ConfigurationSettingMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hongqianhui
 */
@Service
public class ConfigurationSettingServiceImpl extends BaseServiceImpl<ConfigurationSetting,ConfigurationSettingMapper > implements IConfigurationSettingService {

    @Override
    public List<ConfigurationSetting> selectConfigurationInfo(ConfigurationForSelect configurationForSelect) {
        int page = configurationForSelect.page;
        int size = configurationForSelect.limit;
        int startIndex = (page - 1) * 10;
        int endIndex = startIndex + size;
        String codeDes = configurationForSelect.codeDes;
        return  this.baseMapper.selectPageInfo(codeDes , startIndex , endIndex , configurationForSelect.protocolId);
    }

    @Override
    public int selectConfigurationCount(String codeDes, int protocolId) {
        return this.baseMapper.selectLikeCount(codeDes,protocolId);
    }

    @Override
    public void updateFuncode(int protocolId, int fun_code, String opt) {
        this.baseMapper.updateFuncode(protocolId,fun_code,opt);
    }

    @Override
    public List<ConfigurationSetting> selectAll() {
        return this.baseMapper.selectAll();
    }

    @Override
    public void saveOrUpdateBatch(List<ConfigurationSetting> configurationSettings) {
        this.baseMapper.saveOrUpdateBatch(configurationSettings);
    }

    @Override
    public void deleteByProtocolIdAndFuncode(int protocolId, int funCode) {
        this.baseMapper.deleteByProtocolIdAndFuncode(protocolId, funCode);
    }

    @Override
    public void deleteByProtocolId(int protocolId) {
        this.baseMapper.deleteByProtocolId(protocolId);
    }
}
