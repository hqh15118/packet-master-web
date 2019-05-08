package com.zjucsc.application.system.service.iservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjucsc.application.system.entity.OptFilter;
import com.zjucsc.application.domain.exceptions.DeviceNotValidException;
import com.zjucsc.application.domain.exceptions.ProtocolIdNotValidException;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author hongqianhui
 */
public interface IOptFilterService extends IService<OptFilter> {
    CompletableFuture<Exception> addOperationFilter(List<OptFilter.OptFilterForFront> optFilterForFronts) throws ProtocolIdNotValidException;
    CompletableFuture<List<OptFilter>> getTargetExistIdFilter(int deviceId , int type , boolean cached) throws DeviceNotValidException, ProtocolIdNotValidException;
    CompletableFuture<Exception> deleteTargetDeviceFilters(int deviceId);
}
