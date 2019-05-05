package com.zjucsc.application.system.controller;



import com.zjucsc.application.config.Common;
import com.zjucsc.application.domain.analyzer.FiveDimensionAnalyzer;
import com.zjucsc.application.domain.entity.FVDimensionFilterEntity;
import com.zjucsc.application.domain.exceptions.DeviceNotValidException;
import com.zjucsc.application.system.service.iservice.IFiveDimensionFilterService;
import com.zjucsc.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author hongqianhui
 */
@RestController
@RequestMapping("/fv_filter/")
public class FiveDimensionFilterController {

    /**
     * @see com.zjucsc.application.system.service.impl.FiveDimensionFilterServiceImpl
     */
    @Autowired private IFiveDimensionFilterService iFiveDimensionFilterService;

    @ApiOperation("添加/更新五元组过滤规则")
    @PostMapping("new_fv_packet_rule")
    /*-****************************************************
     * FiveDimensionFilterForFront
     * {
     *     device_id : xxx   设备ID         P_K
     *     userName : xxx   用户名         验证
     *     [
     *       {
     *          filterType : 0 / 1        过滤类别：0表示白名单，1表示黑名单
     *          protocolID : 协议ID        过滤的协议，需要转换为String
     *          port ： 端口
     *          src_ip ：
     *          dst_ip ：
     *       },
     *       ...
     *     ]
     * }
     *-***************************************************/
    public BaseResponse addFvDimensionFilterRules(@RequestBody @Valid FVDimensionFilterEntity.FiveDimensionFilterForFront configuration) throws ExecutionException, InterruptedException {
        CompletableFuture<Exception> future =  iFiveDimensionFilterService.addFvDimensionFilter(configuration.getDeviceId(),configuration.getUserName()
        ,configuration.getFiveDimensionFilters());
        if (future.get() == null){
            return BaseResponse.OK();
        }else{
            return BaseResponse.ERROR(Common.HTTP_STATUS_CODE.SYS_ERROR,future.get().getMessage());
        }
    }

    @ApiOperation(value = "查询五元组异常报文规则【当前已配置或历史配置】")
    @GetMapping("/get_fv_packet_rule")
    public BaseResponse loadFvDimensionPacketRule(@RequestParam int deviceId) throws DeviceNotValidException, ExecutionException, InterruptedException {
        CompletableFuture<FVDimensionFilterEntity.FiveDimensionFilterForFront> future =  iFiveDimensionFilterService.getTargetExistIdFilter(deviceId);
        return BaseResponse.OK(future.get());
    }

    @ApiOperation(value = "查询五元组异常报文规则]")
    @DeleteMapping("/delete_fv_packet_rule")
    public BaseResponse deleteFvDimensionPacketRule(@RequestParam int deviceId) throws DeviceNotValidException, ExecutionException, InterruptedException {
        CompletableFuture<Exception> future =  iFiveDimensionFilterService.deleteTargetExistIdAnalyzer(deviceId);
        if (future.get() == null) {
            return BaseResponse.OK();
        }else{
            return BaseResponse.ERROR(Common.HTTP_STATUS_CODE.SYS_ERROR,future.get().getMessage());
        }
    }

    @ApiOperation(value = "查询五元组异常报文规则【已配置的】")
    @GetMapping("/get_fv_packet_rule_cached")
    public BaseResponse loadFvDimensionPacketRuleCached(@RequestParam int deviceId) throws DeviceNotValidException, ExecutionException, InterruptedException {
        CompletableFuture<FVDimensionFilterEntity.FiveDimensionFilterForFront> future =  iFiveDimensionFilterService.getTargetCachedIdAnalyzer(deviceId);
        return BaseResponse.OK(future.get());
    }
}
