package com.zjucsc.application.system.controller;


import com.zjucsc.application.config.Common;
import com.zjucsc.application.system.entity.Device;
import com.zjucsc.application.system.service.iservice.IDeviceService;
import com.zjucsc.application.util.CommonCacheUtil;
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

    @ApiOperation("添加设备信息【保存拓扑图时，删除所有旧设备，添加所有新设备】")
    @PostMapping("new_device")
    public BaseResponse addDeviceInfo(@RequestBody @Valid @NotEmpty List<Device> deviceList){
        iDeviceService.removeAllDevicesByGplotId(Common.GPLOT_ID);
        for (Device device : deviceList) {
            CommonCacheUtil.addOrUpdateDeviceNumberAndIp(device.getDeviceNumber(), device.getDeviceTag());
        }
        iDeviceService.saveBatch(deviceList);
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
