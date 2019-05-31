package com.zjucsc.application.system.service.hessian_iservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjucsc.application.domain.bean.OptFilterForFront;
import com.zjucsc.application.domain.exceptions.DeviceNotValidException;
import com.zjucsc.application.domain.exceptions.OptFilterNotValidException;
import com.zjucsc.application.domain.exceptions.ProtocolIdNotValidException;
import com.zjucsc.application.system.entity.OptFilter;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author hongqianhui
 */
public interface IOptFilterService  {
    CompletableFuture<Exception> addOperationFilter(OptFilterForFront optFilterForFront) throws ProtocolIdNotValidException, OptFilterNotValidException;
    CompletableFuture<List<Integer>> getTargetExistIdFilter(String deviceId, int type, boolean cached, int protocolId) throws DeviceNotValidException, ProtocolIdNotValidException;
    CompletableFuture<Exception> deleteTargetDeviceFilters(int deviceId);

    List<Integer> selectTargetOptFilter(String device, int type, int protocolId);
}
