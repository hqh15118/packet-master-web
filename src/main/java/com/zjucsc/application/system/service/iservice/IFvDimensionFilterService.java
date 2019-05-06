package com.zjucsc.application.system.service.iservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjucsc.application.domain.entity.FvDimensionFilter;
import com.zjucsc.application.domain.exceptions.DeviceNotValidException;

import java.util.List;
import java.util.concurrent.CompletableFuture;


/**
 * @author hongqianhui
 */
public interface IFvDimensionFilterService extends IService<FvDimensionFilter> {
    CompletableFuture<Exception> addFvDimensionFilter(List<FvDimensionFilter> fvDimensionFilters);
    CompletableFuture<List<FvDimensionFilter>> getTargetExistIdFilter(int deviceId , boolean cached) throws DeviceNotValidException;
}