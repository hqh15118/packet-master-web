package com.zjucsc.application.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjucsc.application.domain.entity.OperationFilterEntity;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public interface ConfigurationService extends IService<OperationFilterEntity> {
    //请求已配置好的组态规则
    HashMap<String, Object> loadRule(String userName) throws ExecutionException, InterruptedException;
}
