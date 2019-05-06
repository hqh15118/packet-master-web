package com.zjucsc.application.system.controller;



import com.zjucsc.application.config.Common;
import com.zjucsc.application.domain.entity.OperationFilterEntity;
import com.zjucsc.application.domain.exceptions.DeviceNotValidException;
import com.zjucsc.application.domain.exceptions.ProtocolIdNotValidException;
import com.zjucsc.application.system.service.iservice.IOperationFilterService;
import com.zjucsc.base.BaseResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author hongqianhui
 */
@RestController
@RequestMapping("/gen/operation-filter")
public class OperationFilterController {
    @Autowired private IOperationFilterService iOperationFilterService;

    @ApiOperation(value="配置功能码异常报文规则")
    @RequestMapping(value = "/new_operation_packet_rule" , method = RequestMethod.POST)
    public BaseResponse configOperationPacketRule(@RequestBody @Valid OperationFilterEntity.OperationFilterForFront configuration) throws ProtocolIdNotValidException, ExecutionException, InterruptedException {
        CompletableFuture<Exception> result1 = iOperationFilterService.addOperationFilter(configuration);
        if (result1.get() != null){
            return BaseResponse.ERROR(Common.HTTP_STATUS_CODE.SYS_ERROR,result1.get().getMessage());
        }else {
            return BaseResponse.OK();
        }
    }

    @ApiOperation(value = "查询缓存和数据库中功能码异常报文规则")
    @GetMapping("/get_operation_packet_rule")
    @ResponseBody
    public BaseResponse getOperationPacketRule(@RequestParam int deviceId) throws ExecutionException, InterruptedException, DeviceNotValidException {
       return BaseResponse.OK(iOperationFilterService.getTargetExistIdFilter(deviceId).get());
    }

    @ApiOperation(value = "查询缓存中功能码异常报文规则")
    @GetMapping("/get_operation_packet_rule_cached")
    @ResponseBody
    public BaseResponse getOperationPacketRuleCached(@RequestParam int deviceId) throws DeviceNotValidException, ExecutionException, InterruptedException {
        return BaseResponse.OK(iOperationFilterService.getTargetCachedIdAnalyzer(deviceId).get());
    }
}
