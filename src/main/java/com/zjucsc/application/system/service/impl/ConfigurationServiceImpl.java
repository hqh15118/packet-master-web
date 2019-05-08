package com.zjucsc.application.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjucsc.application.domain.bean.ConfigurationForSelect;
import com.zjucsc.application.domain.bean.ConfigurationWrapper;
import com.zjucsc.application.system.entity.Configuration;
import com.zjucsc.application.system.mapper.ConfigurationMapper;
import com.zjucsc.application.system.service.IProtocolIdService;
import com.zjucsc.application.system.service.iservice.ConfigurationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service("configurationSettingService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ConfigurationServiceImpl extends ServiceImpl<ConfigurationMapper, Configuration> implements ConfigurationService {

    @Override
    public List<Configuration> selectConfigurationInfo(ConfigurationForSelect configurationForSelect) {
        int page = configurationForSelect.page;
        int size = configurationForSelect.limit;
        int startIndex = (page - 1) * 10 + 1;
        int endIndex = startIndex + size;
        String codeDes = configurationForSelect.codeDes;
        return  this.baseMapper.selectPageInfo(codeDes , startIndex , endIndex);
    }

    @Override
    public int selectConfigurationCount(ConfigurationForSelect configurationForSelect) {
        return this.baseMapper.selectCount(configurationForSelect);
    }



}
