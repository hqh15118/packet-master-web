package com.zjucsc.application.system.service.hessian_mapper;

import com.zjucsc.application.domain.bean.DeviceProtocol;
import com.zjucsc.application.system.mapper.base.BaseMapper;


public interface WhiteProtocolMapper extends BaseMapper<DeviceProtocol> {
    DeviceProtocol selectByDeviceNumber(int deviceId);
}
