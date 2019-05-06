package com.zjucsc.application.system.controller;


import com.zjucsc.application.domain.entity.OptFilter;
import com.zjucsc.application.domain.exceptions.DeviceNotValidException;
import com.zjucsc.application.domain.exceptions.ProtocolIdNotValidException;
import com.zjucsc.application.system.service.iservice.IOptFilterService;
import com.zjucsc.base.BaseResponse;
import io.netty.util.concurrent.CompleteFuture;
import io.swagger.annotations.ApiOperation;
import org.dom4j.tree.BaseElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author hongqianhui
 */
@RestController
@RequestMapping("/opt_filter/")
public class OptFilterController {

    @Autowired private IOptFilterService iOptFilterService;


    @ApiOperation("添加功能码规则")
    @PostMapping("new_opt_filter")
    public BaseResponse addNewOptFilter(@RequestBody List<OptFilter.OptFilterForFront> optFilterForFronts) throws ProtocolIdNotValidException, ExecutionException, InterruptedException {
        CompletableFuture<Exception> future =  iOptFilterService.addOperationFilter(optFilterForFronts);
        return BaseResponse.OK(future.get());
    }

    @ApiOperation("获取功能码规则")
    @PostMapping("get_opt_filter")
    public BaseResponse getOptFilter(@RequestParam int deviceId , @RequestParam int type,
                                        @RequestParam int cached) throws ProtocolIdNotValidException, ExecutionException, InterruptedException, DeviceNotValidException {
        boolean cache = false;
        if (cached == 1){
            cache = true;
        }
        CompletableFuture<List<OptFilter>> future =  iOptFilterService.getTargetExistIdFilter(
                deviceId,type,cache
        );
        return BaseResponse.OK(future.get());
    }
}
