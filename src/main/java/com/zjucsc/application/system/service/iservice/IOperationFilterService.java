package com.zjucsc.application.system.service.iservice;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zjucsc.application.domain.entity.OperationFilterEntity;
import com.zjucsc.application.domain.exceptions.DeviceNotValidException;
import com.zjucsc.application.domain.exceptions.ProtocolIdNotValidException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author hongqianhui
 */
public interface IOperationFilterService extends IService<OperationFilterEntity> {
    CompletableFuture<Exception> addOperationFilter(OperationFilterEntity.OperationFilterForFront forFront) throws ProtocolIdNotValidException;
    CompletableFuture<Object> getTargetExistIdFilter(int deviceId) throws DeviceNotValidException;
    CompletableFuture<Object> getTargetCachedIdAnalyzer(int deviceId) throws DeviceNotValidException, ExecutionException, InterruptedException;
}
