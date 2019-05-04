package com.zjucsc.application.system.service.filter;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjucsc.application.domain.entity.FiveDimensionFilterEntity;
import com.zjucsc.application.domain.entity.OperationFilterEntity;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-02 - 20:02
 */
public interface FiveDimensionFilterService extends IService<FiveDimensionFilterEntity> {
    //异步修改组态配置 --> 修改内存
    CompletableFuture<Exception> configFiveDimensionRule(FiveDimensionFilterEntity.FiveDimensionFilterForFront fiveDimensionFilterForFront);
    //请求已配置好的组态规则
    List<FiveDimensionFilterEntity.FiveDimensionFilter> loadRule(String userName);
}
