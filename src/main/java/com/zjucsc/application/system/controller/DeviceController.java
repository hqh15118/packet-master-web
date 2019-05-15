package com.zjucsc.application.system.controller;


import com.zjucsc.application.system.entity.Device;
import com.zjucsc.application.system.service.iservice.IDeviceService;
import com.zjucsc.base.BaseResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<Device> devices = new ArrayList<>();

        for (Device device : deviceList) {
            if (iDeviceService.selectDeviceByIdAndGplot(device.getDeviceNumber(),device.getGPlotId())!=null){
                updateService(device);
                continue;
            }
            devices.add(device);
        }
        iDeviceService.saveBatch(devices);
        return BaseResponse.OK();
    }

    @ApiOperation("修改设备信息")
    public BaseResponse updateService(@RequestBody @Valid  Device device){
        iDeviceService.updateDeviceInfo(device);
        return BaseResponse.OK();
    }

    @ApiOperation("通过组态图ID发现所有设备")
    @GetMapping("gplot_devices")
    public BaseResponse getDevicesByGplotId(@RequestParam int gplotId){
        Map<String,Object> findByGplotId = new HashMap<>();
        findByGplotId.put("gplot_id" , gplotId);
        return BaseResponse.OK(iDeviceService.listByMap(findByGplotId));
    }
}
