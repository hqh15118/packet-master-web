package com.zjucsc.application.system.controller;

import com.zjucsc.application.config.Common;
import com.zjucsc.application.system.entity.FvDimensionFilter;
import com.zjucsc.application.domain.exceptions.DeviceNotValidException;
import com.zjucsc.application.system.service.iservice.IFvDimensionFilterService;
import com.zjucsc.base.BaseResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author hongqianhui
 */
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
@RestController
@RequestMapping("/gen/fv-dimension-filter")
public class FvDimensionFilterController {

    @Autowired private IFvDimensionFilterService iFvDimensionFilterService;

    @ApiOperation("添加/更新五元组过滤规则")
    @PostMapping("new_fv_packet_rule")
    //2019 5 17 -- ok
    public BaseResponse addFvDimensionFilterRules(@RequestBody @Valid @NotEmpty List<FvDimensionFilter> list) throws ExecutionException, InterruptedException {
        CompletableFuture<Exception> future =  iFvDimensionFilterService.addFvDimensionFilter(list);
        if (future.get() == null){
            return BaseResponse.OK();
        }else{
            return BaseResponse.ERROR(Common.HTTP_STATUS_CODE.SYS_ERROR,future.get().getMessage());
        }
    }

    @ApiOperation(value = "查询五元组异常报文规则")
    @GetMapping("/get_fv_packet_rule")
    //2019 5 17 -- ok
    public BaseResponse loadFvDimensionPacketRule(@RequestParam String deviceNumber , @RequestParam String name) throws DeviceNotValidException, ExecutionException, InterruptedException {
        CompletableFuture<List<FvDimensionFilter>> future =  iFvDimensionFilterService.getTargetExistIdFilter(deviceNumber , false);
        return BaseResponse.OK(future.get());
    }
    //2019 5 17 -- ok
    @ApiOperation(value = "查询已经成功挂载的五元组异常报文规则[确认是否已经将规则下载到服务器]")
    @GetMapping("/get_fv_packet_rule_cached")
    public BaseResponse loadFvDimensionPacketRuleCached(@RequestParam String deviceNumber , @RequestParam String name) throws DeviceNotValidException, ExecutionException, InterruptedException {
        CompletableFuture<List<FvDimensionFilter>> future =  iFvDimensionFilterService.getTargetExistIdFilter(deviceNumber , true);
        return BaseResponse.OK(future.get());
    }
}
