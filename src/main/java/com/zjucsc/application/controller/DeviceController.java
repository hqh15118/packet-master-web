package com.zjucsc.application.controller;


import com.zjucsc.application.config.Common;
import com.zjucsc.application.config.auth.Log;
import com.zjucsc.application.domain.bean.Device;
import com.zjucsc.application.domain.bean.StatisticSelect;
import com.zjucsc.application.system.service.hessian_iservice.IDeviceService;
import com.zjucsc.application.util.CommonCacheUtil;
import com.zjucsc.application.domain.bean.BaseResponse;
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
        for (Device device : deviceList) {
            CommonCacheUtil.addOrUpdateDeviceNumberAndTAG(device.getDeviceNumber(), device.getDeviceTag());
        }
        iDeviceService.saveBatch(deviceList);
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

}
