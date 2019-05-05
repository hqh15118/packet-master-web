package com.zjucsc.application.system.controller;


import com.zjucsc.application.domain.bean.FuncodeStatement;
import com.zjucsc.application.domain.entity.FVDimensionFilterEntity;
import com.zjucsc.application.domain.entity.Gplot;
import com.zjucsc.application.domain.entity.OperationFilterEntity;
import com.zjucsc.application.domain.exceptions.DeviceNotValidException;
import com.zjucsc.application.system.service.ConfigurationService;
import com.zjucsc.application.system.service.filter.FiveDimensionFilterService;
import com.zjucsc.application.system.service.filter.OperationFilterService;
import com.zjucsc.base.BaseResponse;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.zjucsc.application.config.Common.CONFIGURATION_MAP;

@Slf4j
@RestController
@RequestMapping(value = "/configuration")
public class ConfigurationController {

    @Autowired private ConfigurationService configurationService;
    @Autowired private OperationFilterService operationFilterService;
    @Autowired private FiveDimensionFilterService fiveDimensionFilterService;

    @ApiOperation(value="配置功能码异常报文规则")
    @RequestMapping(value = "/new_operation_packet_rule" , method = RequestMethod.POST)
    public BaseResponse configOperationPacketRule(@RequestBody @Valid OperationFilterEntity.OperationFilterForFront configuration){
        CompletableFuture<Exception> result1 = operationFilterService.configOperationRule(configuration);
        return validResult(result1,configuration);
    }

    @ApiOperation(value = "查询所有异常报文规则")
    @GetMapping(value = "/get_all_packet_rule")
    public HashMap<String, Object> getAllPacketRule(@RequestParam int deviceId) throws ExecutionException, InterruptedException, DeviceNotValidException {
        return configurationService.loadRule(deviceId);
    }

    @ApiOperation(value = "查询所有组态")
    @GetMapping(value = "/get_configuration")
    public HashMap<String , List<FuncodeStatement>> getAllConfiguration(){
        return CONFIGURATION_MAP;
    }

    @ApiOperation(value = "配置五元组异常报文规则")
    @PostMapping("/new_fv_packet_rule")
    public BaseResponse configFvDimensionPacketRule(@RequestBody @Valid FVDimensionFilterEntity.FiveDimensionFilterForFront configuration){
        CompletableFuture<Exception> result1 = fiveDimensionFilterService.configFiveDimensionRule(configuration);
        return validResult(result1,configuration);
    }

    @ApiOperation(value = "查询五元组异常报文规则]")
    @GetMapping("/get_fv_packet_rule")
    public List<FVDimensionFilterEntity.FiveDimensionFilter> loadFvDimensionPacketRule(@RequestParam int deviceId) throws DeviceNotValidException {
        return fiveDimensionFilterService.loadRule(deviceId);
    }

    @ApiOperation(value = "查询功能码异常报文规则]")
    @GetMapping("/get_operation_packet_rule")
    public OperationFilterEntity.OperationFilterForFront loadOperationPacketRule(@RequestParam String userName , @RequestParam String protocol){
        return operationFilterService.loadRule(userName,protocol);
    }

    @ApiOperation(value = "查询所有功能码异常报文规则]")
    @GetMapping("/get_all_operation_packet_rule")
    public List<OperationFilterEntity.OperationFilterForFront> loadAllOperationPacketRule(@RequestParam int deviceId) throws ExecutionException, InterruptedException {
        return operationFilterService.loadAllRule(deviceId).get();
    }

    private <T> BaseResponse validResult(CompletableFuture<Exception> exceptionCompletableFuture , T t){
        try {
            Exception exception = null;
            if ((exception = exceptionCompletableFuture.get()) == null) {
                log.info(" 更新/保存组态 ： \n {}  -- 成功" , t);
                return BaseResponse.OK();
            }else{
                log.info(" 更新/保存组态 ： \n {}  -- 失败" , t , exception);
                return BaseResponse.ERROR(500,exception.getMessage());
            }
        } catch (InterruptedException | ExecutionException e) {
            log.info(" 更新/保存组态 ： \n {}  -- 失败" , t , e);
            return BaseResponse.ERROR(500,e.getMessage());
        }
    }

}
