package com.zjucsc.application.system.service.iservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjucsc.application.domain.bean.OptFilterForFront;
import com.zjucsc.application.domain.exceptions.OptFilterNotValidException;
import com.zjucsc.application.system.entity.OptFilter;
import com.zjucsc.application.domain.exceptions.DeviceNotValidException;
import com.zjucsc.application.domain.exceptions.ProtocolIdNotValidException;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author hongqianhui
 */
public interface IOptFilterService extends IService<OptFilter> {
    CompletableFuture<Exception> addOperationFilter(OptFilterForFront optFilterForFront) throws ProtocolIdNotValidException, OptFilterNotValidException;
    CompletableFuture<List<Integer>> getTargetExistIdFilter(int deviceId , int type , boolean cached , int protocolId) throws DeviceNotValidException, ProtocolIdNotValidException;
    CompletableFuture<Exception> deleteTargetDeviceFilters(int deviceId);

    List<Integer> selectTargetOptFilter(int device , int type ,int protocolId);
}
