package com.zjucsc.application.system.service.hessian_iservice;

import com.zjucsc.application.domain.bean.OptFilterForFront;
import com.zjucsc.application.domain.exceptions.DeviceNotValidException;
import com.zjucsc.application.domain.exceptions.OptFilterNotValidException;
import com.zjucsc.application.domain.exceptions.ProtocolIdNotValidException;

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
    void deleteByDeviceNumber(String deviceNumber);
    void deleteByDeviceNumberAndProtocolId(String deviceNumber,int protocolId);
    void deleteByDeviceNumberAndPorocolIdAndFuncode(String deviceNumber,int protocolId,int funCode);
}
