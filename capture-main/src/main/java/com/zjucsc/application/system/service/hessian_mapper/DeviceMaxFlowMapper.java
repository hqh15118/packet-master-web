package com.zjucsc.application.system.service.hessian_mapper;

import com.zjucsc.application.domain.non_hessian.DeviceMaxFlow;

import java.util.List;

public interface DeviceMaxFlowMapper {
    void insertByDeviceNumber(DeviceMaxFlow deviceMaxFlow);
    void updateByDeviceNumber(DeviceMaxFlow deviceMaxFlow);
    DeviceMaxFlow selectByDeviceNumber(String deviceNumber);
    List<DeviceMaxFlow> selectBatch();
    DeviceMaxFlow deleteByDeviceNumber(String deviceNumber);
}
