package com.zjucsc.application.system.service.iservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjucsc.application.domain.entity.OperationFilterEntity;
import com.zjucsc.application.domain.exceptions.DeviceNotValidException;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public interface ConfigurationService extends IService<OperationFilterEntity> {
    //请求已配置好的组态规则
    CompletableFuture<HashMap<String, Object>> loadRule(int deviceId) throws ExecutionException, InterruptedException, DeviceNotValidException;

    //请求已配置好的或者数据库中的组态规则
    CompletableFuture<HashMap<String, Object>> loadRuleAll(int deviceId) throws ExecutionException, InterruptedException, DeviceNotValidException;
}
