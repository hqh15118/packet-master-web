package com.zjucsc.application.system.service.hessian_impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjucsc.application.domain.bean.ConfigurationForSelect;
import com.zjucsc.application.system.entity.ConfigurationSetting;
import com.zjucsc.application.system.mapper.base.BaseServiceImpl;
import com.zjucsc.application.system.service.hessian_iservice.IConfigurationSettingService;
import com.zjucsc.application.system.service.hessian_mapper.ConfigurationSettingMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author hongqianhui
 */
@Service
public class ConfigurationSettingServiceImpl extends BaseServiceImpl<ConfigurationSettingMapper, ConfigurationSetting> implements IConfigurationSettingService {

    @Transactional
    @Override
    public List<ConfigurationSetting> selectConfigurationInfo(ConfigurationForSelect configurationForSelect) {
        int page = configurationForSelect.page;
        int size = configurationForSelect.limit;
        int startIndex = (page - 1) * 10;
        int endIndex = startIndex + size;
        String codeDes = configurationForSelect.codeDes;
        return  this.baseMapper.selectPageInfo(codeDes , startIndex , endIndex , configurationForSelect.protocolId);
    }
    @Transactional
    @Override
    public int selectConfigurationCount(String codeDes, int protocolId) {
        return this.baseMapper.selectLikeCount(codeDes,protocolId);
    }

    @Override
    public void updateFuncode(int protocolId, int fun_code, String opt) {
        this.baseMapper.updateFuncode(protocolId,fun_code,opt);
    }
}
