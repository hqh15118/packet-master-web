package com.zjucsc.application.system.service.filter;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjucsc.application.domain.entity.OperationFilterEntity;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-02 - 20:34
 */
public interface OperationFilterService extends IService<OperationFilterEntity> {
    //异步修改组态配置 --> 修改内存
    CompletableFuture<Exception> configOperationRule(OperationFilterEntity.OperationFilterForFront configuration);
    //请求已配置好的组态规则
    OperationFilterEntity.OperationFilterForFront loadRule(String userName , String protocol);

    CompletableFuture<List<OperationFilterEntity.OperationFilterForFront>> loadAllRule(int deviceId);
}
