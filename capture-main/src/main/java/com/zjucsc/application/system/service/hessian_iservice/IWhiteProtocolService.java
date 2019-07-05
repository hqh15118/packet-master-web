package com.zjucsc.application.system.service.hessian_iservice;

import com.zjucsc.application.domain.bean.DeviceProtocol;
import com.zjucsc.application.system.mapper.base.IService;

public interface IWhiteProtocolService extends IService<DeviceProtocol> {

    DeviceProtocol selectByDeviceNumber(int deviceId);
}
