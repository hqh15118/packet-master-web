package com.zjucsc.application.controller;


import com.zjucsc.application.config.Common;
import com.zjucsc.application.config.auth.Log;
import com.zjucsc.application.domain.bean.BaseResponse;
import com.zjucsc.application.domain.bean.Device;
import com.zjucsc.application.domain.bean.StatisticSelect;
import com.zjucsc.application.system.service.hessian_iservice.IDeviceService;
import com.zjucsc.application.util.CommonCacheUtil;
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

    @Log
    @ApiOperation("添加设备信息【保存拓扑图时，删除所有旧设备，添加所有新设备】")
    @PostMapping("new_device")
    public BaseResponse addDeviceInfo(@RequestBody @Valid @NotEmpty List<Device> deviceList){
        iDeviceService.removeAllDevicesByGplotId(Common.GPLOT_ID);
        CommonCacheUtil.removeAllCachedDeviceNumber();
        CommonCacheUtil.removeAllDeviceNumberToName();
        for (Device device : deviceList) {
            if (Common.filterStatement == 0) {
                CommonCacheUtil.addOrUpdateDeviceNumberAndTAG(device.getDeviceNumber(), device.getDeviceTag());
                CommonCacheUtil.addDeviceNumberToName(device.getDeviceNumber(),device.getDeviceInfo());
                CommonCacheUtil.addOrUpdateDeviceManually(device);
            }else{
                CommonCacheUtil.addOrUpdateDeviceNumberAndTAG(device.getDeviceNumber(), device.getDeviceTag());
                CommonCacheUtil.addDeviceNumberToName(device.getDeviceNumber(),device.getDeviceInfo());
                CommonCacheUtil.addOrUpdateDeviceManually(device);
            }
        }
        iDeviceService.saveBatch(deviceList);
        return BaseResponse.OK();
    }

    @Log
    @ApiOperation("添加/修改设备信息")
    @PostMapping("update_device")
    public BaseResponse addOrUpdateDeviceInfo(@RequestBody Device device){
        iDeviceService.saveOrUpdateDevice(device);
        return BaseResponse.OK();
    }

    @Log
    @ApiOperation("删除设备")
    @DeleteMapping("delete_device")
    public BaseResponse deleteDevice(@RequestParam String deviceNumber){
        Device device = iDeviceService.removeDevice(deviceNumber);
        CommonCacheUtil.removeDeviceNumberToTag(deviceNumber);
        CommonCacheUtil.removeDeviceNumberToName(deviceNumber);
        CommonCacheUtil.removeAllDeviceListByMacAddress(device.getDeviceTag());
        return BaseResponse.OK();
    }

    @Log
    @ApiOperation("通过组态图ID发现所有设备")
    @GetMapping("gplot_devices")
    public BaseResponse getDevicesByGplotId(@RequestParam int gplotId){
        return BaseResponse.OK(iDeviceService.selectByGplotId(gplotId));
    }

    @Log
    @ApiOperation("获取设备统计信息")
    @PostMapping("statistic")
    public BaseResponse getDeviceHistoryRuninfo(@RequestBody StatisticSelect statisticSelect){
        return BaseResponse.OK(iDeviceService.selectHistoryDeviceRunInfo(
                statisticSelect.getDeviceId(),
                statisticSelect.getStart(),
                statisticSelect.getEnd(),
                statisticSelect.getIntervalType()
        ));
    }

    @ApiOperation("修改设备配置状态")
    @GetMapping("config_state")
    public BaseResponse changeDeviceConfigState(@RequestParam String deviceNumber , @RequestParam boolean isConfig){
        iDeviceService.changeDeviceConfigState(deviceNumber , isConfig);
        return BaseResponse.OK();
    }

    @ApiOperation("获取所有配置过的设备")
    @GetMapping("configured_devices")
    public BaseResponse getAllConfiguredDevices(){
        return BaseResponse.OK(iDeviceService.selectAllConfiguredDevices());
    }

}
