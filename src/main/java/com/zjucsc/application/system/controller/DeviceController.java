package com.zjucsc.application.system.controller;


import com.zjucsc.application.system.entity.Device;
import com.zjucsc.application.system.service.iservice.IDeviceService;
import com.zjucsc.base.BaseResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author hongqianhui
 */
@RestController
@RequestMapping("/device/")
public class DeviceController {
    @Autowired private IDeviceService iDeviceService;

    @ApiOperation("添加设备信息")
    @PostMapping("new_device")
    public BaseResponse addDeviceInfo(@RequestBody @Valid @NotEmpty List<Device> deviceList){
        iDeviceService.saveBatch(deviceList);
        return BaseResponse.OK();
    }

    @ApiOperation("修改设备信息")
    @RequestMapping(value = "update_device" , method = RequestMethod.POST)
    public BaseResponse updateService(@RequestBody Device device){
        iDeviceService.saveOrUpdate(device);
        return BaseResponse.OK();
    }
}
