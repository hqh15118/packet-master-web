package com.zjucsc.application.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjucsc.application.domain.entity.ConfigurationSetting;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ConfigurationService extends IService<ConfigurationSetting> {
    //异步保存组态配置
    CompletableFuture<Exception> saveRule(List<ConfigurationSetting.Configuration> configurations);
    //异步修改组态配置
    CompletableFuture<Exception> configRule(List<ConfigurationSetting.Configuration> configurations);
    //
    List<ConfigurationSetting> doLoadRule();

    HashMap<String,List<ConfigurationSetting.ConfigurationContent>> loadRule();
}
