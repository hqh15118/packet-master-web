package com.zjucsc.application.system.controller;


import com.zjucsc.application.domain.entity.Device;
import com.zjucsc.application.system.service.iservice.IDeviceService;
import com.zjucsc.base.BaseResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author hongqianhui
 */
@RestController
@RequestMapping("/device/")
public class DeviceController {
    @Qualifier("deviceservice")
    @Autowired
    private IDeviceService iDeviceService;

    @ApiOperation("添加设备信息")
    @PostMapping("new_device")
    public BaseResponse addDeviceInfo(@RequestBody @Valid Device device){
        iDeviceService.save(device);
        return BaseResponse.OK();
    }

//    @ApiOperation("加载组态图设备位置信息")
//    @GetMapping("load_gplot")
//    public Gplot loadGplotInfo(@RequestParam int id) throws RuntimeException {
//        Gplot gplot = iGplotService.getById(id);
//        if (gplot == null){
//            throw new RuntimeException(id + " 不存在");
//        }
//        System.out.println(gplot);
//        return gplot;
//    }

    @ApiOperation("修改设备信息")
    @RequestMapping(value = "update_device" , method = RequestMethod.POST)
    public BaseResponse updateGplotInfo(@RequestBody Device device){
        iDeviceService.saveOrUpdate(device);
        return BaseResponse.OK();
    }

//    @ApiOperation("删除组态图设备位置信息")
//    @DeleteMapping("delete_device")
//    public BaseResponse deleteGplotInfo(@RequestParam int gplotId){
//        iDeviceService.removeById(gplotId);
//        return BaseResponse.OK();
//    }
}
