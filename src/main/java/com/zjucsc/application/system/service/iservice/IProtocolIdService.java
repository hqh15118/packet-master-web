package com.zjucsc.application.system.service.iservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjucsc.application.system.entity.ProtocolId;

/**
 * @author hongqianhui
 */
public interface IProtocolIdService extends IService<ProtocolId> {
    int getMax();
}
