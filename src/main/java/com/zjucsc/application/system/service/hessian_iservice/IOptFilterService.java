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
    CompletableFuture<Exception> addOperationFilter(OptFilterForFront optFilterForFront) throws ProtocolIdNotValidException, OptFilterNotValidException, DeviceNotValidException;
    CompletableFuture<List<Integer>> getTargetExistIdFilter(String deviceId, boolean cached, int protocolId) throws DeviceNotValidException, ProtocolIdNotValidException;

    List<Integer> selectTargetOptFilter(String device, int protocolId);
    void deleteByDeviceNumber(String deviceNumber);
    void deleteByDeviceNumberAndProtocolId(String deviceNumber,int protocolId);
    void deleteByDeviceNumberAndProtocolIdAndFuncode(String deviceNumber,int protocolId,int funCode);
}
