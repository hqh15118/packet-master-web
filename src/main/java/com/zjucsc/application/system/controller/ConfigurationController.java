package com.zjucsc.application.system.controller;


import com.zjucsc.application.domain.bean.FuncodeStatement;
import com.zjucsc.application.domain.exceptions.DeviceNotValidException;
import com.zjucsc.application.system.service.iservice.ConfigurationService;
import com.zjucsc.base.BaseResponse;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.zjucsc.application.config.Common.CONFIGURATION_MAP;

@Slf4j
@RestController
@RequestMapping(value = "/configuration")
public class ConfigurationController {

    @Autowired private ConfigurationService configurationService;
    //@Autowired private OperationFilterService operationFilterService;
    //@Autowired private FiveDimensionFilterService fiveDimensionFilterService;

//    @ApiOperation(value="配置功能码异常报文规则")
//    @RequestMapping(value = "/new_operation_packet_rule" , method = RequestMethod.POST)
//    public BaseResponse configOperationPacketRule(@RequestBody @Valid OperationFilterEntity.OperationFilterForFront configuration){
//        CompletableFuture<Exception> result1 = operationFilterService.configOperationRule(configuration);
//        return validResult(result1,configuration);
//    }

    @ApiOperation(value = "查询所有已配置异常报文规则")
    @GetMapping(value = "/get_all_packet_rule_cached")
    public BaseResponse getAllPacketRuleCached(@RequestParam int deviceId) {
        //HashMap map = configurationService.loadRule(deviceId).get();
        return BaseResponse.OK(deviceId);
    }

    @ApiOperation(value = "查询所有组态")
    @GetMapping(value = "/get_configuration")
    public BaseResponse getAllConfiguration(){
        HashMap<String,List<FuncodeStatement>> listHashMap = new HashMap<>();
        for (String protocol : CONFIGURATION_MAP.keySet()){
            List<FuncodeStatement> funcodeStatementList = new ArrayList<>();
            HashMap<Integer,String> map = CONFIGURATION_MAP.get(protocol);
            for (int fun_code : map.keySet()){
                funcodeStatementList.add(new FuncodeStatement(fun_code,map.get(fun_code)));
            }
            listHashMap.put(protocol,funcodeStatementList);
        }
        return BaseResponse.OK(listHashMap);
    }

    @ApiOperation(value = "查询所有已配置或数据库中未配置的异常报文规则")
    @GetMapping(value = "/get_all_packet_rule")
    public BaseResponse getAllPacketRule(@RequestParam int deviceId) throws ExecutionException, InterruptedException, DeviceNotValidException {
        //HashMap map = configurationService.loadRuleAll(deviceId).get();
        return BaseResponse.OK(null);
    }
}
