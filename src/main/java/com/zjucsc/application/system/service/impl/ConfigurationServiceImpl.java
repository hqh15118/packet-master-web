package com.zjucsc.application.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjucsc.application.system.dao.ConfigurationMapper;
import com.zjucsc.application.domain.entity.ConfigurationSetting;
import com.zjucsc.application.system.service.ConfigurationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.zjucsc.application.config.Common.BAD_PACKET_FILTER;


@Slf4j
@Service("configurationSettingService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ConfigurationServiceImpl extends ServiceImpl<ConfigurationMapper, ConfigurationSetting> implements ConfigurationService {
    @Async("global_single_thread_executor")
    public CompletableFuture<Exception> saveRule(List<ConfigurationSetting.Configuration> configurations){
        for (ConfigurationSetting.Configuration configuration : configurations) {
            String configurationContent = JSON.toJSONString(configuration.getConfigurations());
            ConfigurationSetting setting = new ConfigurationSetting();
            setting.setContent(configurationContent);
            setting.setProtocol(configuration.getProtocol());
            save(setting);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Exception> configRule(List<ConfigurationSetting.Configuration> configurations) {
        HashMap<String,List<ConfigurationSetting.ConfigurationContent>> map = new HashMap<>();
        for (ConfigurationSetting.Configuration configuration : configurations) {
            map.put(configuration.getProtocol(),configuration.getConfigurations());
        }
        BAD_PACKET_FILTER = map;
        return CompletableFuture.completedFuture(null);
    }


    @Override
    public List<ConfigurationSetting> doLoadRule() {
        return this.baseMapper.loadAllRule();
    }

    private final byte[] LOCK = new byte[1];

    @Override
    public HashMap<String, List<ConfigurationSetting.ConfigurationContent>> loadRule() {
        if (BAD_PACKET_FILTER.size() == 0){
            synchronized (LOCK){
                if (BAD_PACKET_FILTER.size() == 0) {
                    List<ConfigurationSetting> configurationSettings = doLoadRule();
                    for (ConfigurationSetting configurationSetting : configurationSettings) {
                        BAD_PACKET_FILTER.put(configurationSetting.getProtocol(), JSON.parseArray(configurationSetting.getContent()
                                , ConfigurationSetting.ConfigurationContent.class));
                    }
                }
            }
        }
        return BAD_PACKET_FILTER;
    }
}
