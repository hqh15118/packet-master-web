package com.zjucsc.application.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjucsc.application.system.dao.ConfigurationMapper;
import com.zjucsc.application.domain.entity.ConfigurationSetting;
import com.zjucsc.application.system.service.ConfigurationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service("configurationSettingService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ConfigurationServiceImpl extends ServiceImpl<ConfigurationMapper, ConfigurationSetting> implements ConfigurationService {
}
