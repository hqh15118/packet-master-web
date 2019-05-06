package com.zjucsc.application.system.service.iservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjucsc.application.domain.entity.OperationFilterEntity;
import com.zjucsc.application.domain.entity.OptFilter;
import com.zjucsc.application.domain.exceptions.DeviceNotValidException;
import com.zjucsc.application.domain.exceptions.ProtocolIdNotValidException;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author hongqianhui
 */
public interface IOptFilterService extends IService<OptFilter> {
    CompletableFuture<Exception> addOperationFilter(HashMap<Integer,List<OptFilter>> optFiltersMap) throws ProtocolIdNotValidException;
    CompletableFuture<List<OptFilter>> getTargetExistIdFilter(int deviceId , int type , boolean cached) throws DeviceNotValidException;
}
