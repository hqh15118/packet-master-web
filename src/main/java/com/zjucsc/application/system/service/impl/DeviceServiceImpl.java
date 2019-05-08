package com.zjucsc.application.system.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjucsc.application.system.entity.Device;
import com.zjucsc.application.system.mapper.DeviceMapper;
import com.zjucsc.application.system.service.iservice.IDeviceService;
import org.springframework.stereotype.Service;

/**
 * @author hongqianhui
 */
@Service("deviceservice")
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements IDeviceService {

}
