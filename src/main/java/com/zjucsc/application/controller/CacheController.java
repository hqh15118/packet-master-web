package com.zjucsc.application.controller;


import com.zjucsc.application.config.Common;
import com.zjucsc.application.domain.bean.BaseResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cache")
public class CacheController {

    @ApiOperation("获取组态图ID")
    @GetMapping("getCache")
    public BaseResponse getCachedData(){
        return BaseResponse.OK(Common.GPLOT_ID);
    }
}
