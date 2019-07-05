package com.zjucsc.application.system.service.hessian_impl;

import com.zjucsc.application.domain.bean.DeviceProtocol;
import com.zjucsc.application.system.mapper.base.BaseServiceImpl;
import com.zjucsc.application.system.service.hessian_iservice.IWhiteProtocolService;
import com.zjucsc.application.system.service.hessian_mapper.WhiteProtocolMapper;
import org.springframework.stereotype.Service;

@Service
public class WhiteProtocolServiceImpl extends BaseServiceImpl<DeviceProtocol, WhiteProtocolMapper> implements IWhiteProtocolService {

    @Override
    public DeviceProtocol selectByDeviceNumber(int deviceId) {
        return this.baseMapper.selectByDeviceNumber(deviceId);
    }
}
