package com.zjucsc.application.controller;


import com.zjucsc.application.domain.bean.BaseResponse;
import com.zjucsc.application.util.CommonCacheUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("sys")
public class SysController {

    @GetMapping("cpu_mem_rate")
    public BaseResponse setCpuAndMemState(@RequestParam double cpuRate,@RequestParam double memRate){
        CommonCacheUtil.updateCpuOrMemoryRate(cpuRate, memRate);
        return BaseResponse.OK();
    }

    @ApiOperation("CPU和内存报警参数，count*2=秒数")
    @GetMapping("cpu_mem_count")
    public BaseResponse setCpuAndMemCount(@RequestParam(defaultValue = "30") int cpuCount,@RequestParam(defaultValue = "30") int memCount){
        CommonCacheUtil.updateCpuOrMemCount(cpuCount, memCount);
        return BaseResponse.OK();
    }

    @GetMapping("all_state")
    public BaseResponse getAllRunStateArgs(){
        return BaseResponse.OK(CommonCacheUtil.getSysRunConfig());
    }


}
