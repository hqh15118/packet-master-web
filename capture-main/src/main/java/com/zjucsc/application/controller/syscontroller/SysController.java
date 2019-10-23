package com.zjucsc.application.controller.syscontroller;


import com.zjucsc.application.domain.bean.BaseResponse;
import com.zjucsc.application.util.CacheUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("sys")
public class SysController {

    @GetMapping("cpu_mem_rate")
    public BaseResponse setCpuAndMemState(@RequestParam double cpuRate,@RequestParam double memRate){
        CacheUtil.updateCpuOrMemoryRate(cpuRate, memRate);
        return BaseResponse.OK();
    }

    @ApiOperation("CPU和内存报警参数，count*2=秒数")
    @GetMapping("cpu_mem_count")
    public BaseResponse setCpuAndMemCount(@RequestParam(defaultValue = "30") int cpuCount,@RequestParam(defaultValue = "30") int memCount){
        CacheUtil.updateCpuOrMemCount(cpuCount, memCount);
        return BaseResponse.OK();
    }

    @GetMapping("all_state")
    public BaseResponse getAllRunStateArgs(){
        return BaseResponse.OK(CacheUtil.getSysRunConfig());
    }


}
