package com.zjucsc.application.system.service.hessian_iservice;

import com.zjucsc.application.domain.bean.Rule;
import com.zjucsc.common.exceptions.DeviceNotValidException;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author hongqianhui
 */
public interface IFvDimensionFilterService {
    /**
     * 向数据库中批量插入
     * @param rules
     * @return
     */
    CompletableFuture<Exception> addFvDimensionFilter(List<Rule> rules);
    CompletableFuture<List<Rule>> getTargetExistIdFilter(String deviceId, boolean cached) throws DeviceNotValidException;
    void deleteAllFilterByDeviceNumberAndGplotId(String deviceNumber, int gplotId);
}
