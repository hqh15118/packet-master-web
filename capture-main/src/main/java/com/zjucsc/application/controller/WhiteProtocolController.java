package com.zjucsc.application.controller;


import com.zjucsc.application.config.auth.Log;
import com.zjucsc.application.domain.bean.BaseResponse;
import com.zjucsc.application.domain.bean.DeviceProtocol;
import com.zjucsc.application.system.service.hessian_iservice.IWhiteProtocolService;
import com.zjucsc.application.util.CacheUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/white_protocol/")
public class WhiteProtocolController {

    @Autowired private IWhiteProtocolService iWhiteProtocolService;

    @Log
    @ApiOperation("添加协议白名单")
    @PostMapping("add_rp")
    public BaseResponse addRightProtocolToCache(@RequestBody DeviceProtocol rightProtocols){
        CacheUtil.addWhiteProtocolToCache(rightProtocols.getDeviceNumber(),rightProtocols.getProtocolName());
        iWhiteProtocolService.insertById(rightProtocols);
        return BaseResponse.OK();
    }

    @Log
    @ApiOperation("删除协议白名单")
    @PostMapping("del_rp")
    public BaseResponse delRightProtocolToCache(@RequestBody DeviceProtocol rightProtocols){
        int count = CacheUtil.removeWhiteProtocolFromCache(rightProtocols.getDeviceNumber(),rightProtocols.getProtocolName());
        iWhiteProtocolService.deleteById(rightProtocols);
        return BaseResponse.OK(count);
    }

    @Log
    @ApiOperation("查询协议白名单")
    @GetMapping("find_all_rp")
    public BaseResponse selectRightProtocolToCache(@RequestParam int deviceId){
        return BaseResponse.OK(iWhiteProtocolService.selectByDeviceNumber(deviceId));
    }
}
