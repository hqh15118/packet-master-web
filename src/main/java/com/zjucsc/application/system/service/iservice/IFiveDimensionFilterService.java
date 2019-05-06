package com.zjucsc.application.system.service.iservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjucsc.application.domain.analyzer.FiveDimensionAnalyzer;
import com.zjucsc.application.domain.entity.FVDimensionFilterEntity;
import com.zjucsc.application.domain.exceptions.DeviceNotValidException;
import com.zjucsc.base.BaseResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author hongqianhui
 */
public interface IFiveDimensionFilterService extends IService<FVDimensionFilterEntity> {
    CompletableFuture<Exception> addFvDimensionFilter(int deviceId , String userName , List<FVDimensionFilterEntity.FiveDimensionFilter> fiveDimensionFilters);
    CompletableFuture<FVDimensionFilterEntity.FiveDimensionFilterForFront> getTargetExistIdFilter(int deviceId) throws DeviceNotValidException;
    CompletableFuture<FVDimensionFilterEntity.FiveDimensionFilterForFront> getTargetCachedIdAnalyzer(int deviceId) throws DeviceNotValidException, ExecutionException, InterruptedException;
    CompletableFuture<Exception> deleteTargetExistIdAnalyzer(int deviceId) throws DeviceNotValidException;
}
