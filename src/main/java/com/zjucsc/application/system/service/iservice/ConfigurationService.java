package com.zjucsc.application.system.service.iservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjucsc.application.domain.entity.Configuration;

public interface ConfigurationService extends IService<Configuration> {
    //请求已配置好的或者数据库中的组态规则
    //CompletableFuture<HashMap<String, Object>> loadRuleAll(int deviceId) throws ExecutionException, InterruptedException, DeviceNotValidException;
    //CompletableFuture<Exception> addNewConfiguration(List<String> )
}
