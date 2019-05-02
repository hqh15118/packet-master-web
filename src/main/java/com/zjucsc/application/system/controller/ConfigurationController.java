package com.zjucsc.application.system.controller;


import com.zjucsc.application.domain.entity.ConfigurationSetting;
import com.zjucsc.application.system.service.ConfigurationService;
import com.zjucsc.base.BaseResponse;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import static com.zjucsc.application.config.Common.BAD_PACKET_FILTER;
import static com.zjucsc.application.config.Common.CONFIGURATION_MAP;

@Slf4j
@RestController
@RequestMapping(value = "/configuration")
public class ConfigurationController {

    @Autowired
    private ConfigurationService configurationService;

    @ApiOperation(value="配置组态【异常报文规则】")
    @RequestMapping(value = "/new_packet_rule" , method = RequestMethod.POST)
    public BaseResponse addPacketRule(@RequestBody @Valid List<ConfigurationSetting.Configuration> configurations){
        CompletableFuture<Exception> result1 = configurationService.configRule(configurations);
        CompletableFuture<Exception> result2 = configurationService.saveRule(configurations);
        try {
            Exception exception = null;
            if ((exception = result1.get()) == null && (exception = result2.get()) == null) {
                return BaseResponse.OK();
            }else{
                return BaseResponse.ERROR(500,exception.getMessage());
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("" , e);
            return BaseResponse.ERROR(500,e.getMessage());
        }
    }

    @ApiOperation(value = "查询所有组态【异常报文规则】")
    @GetMapping(value = "/get_packet_rule")
    public HashMap<String , List<ConfigurationSetting.ConfigurationContent>> getAllPacketRule(){
        if (BAD_PACKET_FILTER.size() == 0){

        }
        return BAD_PACKET_FILTER;
    }

    @ApiOperation(value = "查询所有组态【某协议下功能码对应的含义】")
    @GetMapping(value = "/get_configuration")
    public HashMap<String , Map<Integer,String>> getAllConfiguration(){
        return CONFIGURATION_MAP;
    }
}
