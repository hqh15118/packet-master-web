package com.zjucsc.application.system.controller;


import com.zjucsc.application.config.Common;
import com.zjucsc.application.domain.bean.ConfigurationForFront;
import com.zjucsc.application.domain.bean.FuncodeStatement;
import com.zjucsc.application.domain.entity.Configuration;
import com.zjucsc.application.domain.exceptions.DeviceNotValidException;
import com.zjucsc.application.system.service.iservice.ConfigurationService;
import com.zjucsc.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import static com.zjucsc.application.config.Common.CONFIGURATION_MAP;

@Slf4j
@RestController
@RequestMapping(value = "/configuration")
public class ConfigurationController {

    @Autowired private ConfigurationService configurationService;

    @ApiOperation(value = "查询所有已配置异常报文规则")
    @GetMapping(value = "/get_all_packet_rule_cached")
    public BaseResponse getAllPacketRuleCached(@RequestParam int deviceId) {
        //HashMap map = configurationService.loadRule(deviceId).get();
        return BaseResponse.OK(deviceId);
    }

    @ApiOperation(value = "查询所有功能码")
    @GetMapping(value = "/get_opt")
    public BaseResponse getAllConfiguration(){
        HashMap<String,ConfigurationForFront> map = new HashMap<>();
        List<Configuration> configurations = configurationService.list();
        for (Configuration configuration : configurations) {
            String protocolName = Common.PROTOCOL_STR_TO_INT.get(configuration.getFun_code());
            map.computeIfAbsent(protocolName, s -> {
                ConfigurationForFront configurationForFront = new ConfigurationForFront();
                configurationForFront.setProtocol(protocolName);
                configurationForFront.setConfigurationWrappers(new ArrayList<>());
                return configurationForFront;
            });
            map.get(protocolName).getConfigurationWrappers().add(new
                    ConfigurationForFront.ConfigurationWrapper(configuration.getFun_code(),configuration.getOpt()));
        }
        return BaseResponse.OK(map.values());
    }

    @ApiOperation(value = "添加自定义功能码")
    @PostMapping(value = "/add_opt")
    public BaseResponse addConfiguration(@RequestBody @Valid @NotEmpty List<ConfigurationForFront> configurationForFronts){
        List<Configuration> list = convertFrontToEntity(configurationForFronts);
        for (Configuration configuration : list) {
            Common.CONFIGURATION_MAP.get(Common.PROTOCOL_STR_TO_INT.get(configuration.getProtocolId())).put(configuration.getFun_code(),
                    configuration.getOpt());
        }
        configurationService.saveOrUpdateBatch(list);
        return BaseResponse.OK();
    }

    @ApiOperation(value = "删除功能码")
    @GetMapping(value = "/delete_opt")
    public BaseResponse deleteConfiguration(List<ConfigurationForFront> configurationForFronts){
        List<Configuration> list = convertFrontToEntity(configurationForFronts);
        HashMap<String,Object> map = new HashMap<>();
        for (Configuration configuration : list) {
            map.clear();
            map.put("protocol_id" , configuration.getProtocolId());
            map.put("fun_code" , configuration.getFun_code());
            configurationService.removeByMap(map);
            Common.CONFIGURATION_MAP.get(Common.PROTOCOL_STR_TO_INT.get(configuration.getProtocolId())).remove(configuration.getFun_code());
        }
        return BaseResponse.OK();
    }


    private List<Configuration> convertFrontToEntity(List<ConfigurationForFront> configurationForFronts){
        List<Configuration> list = new ArrayList<>();
        for (ConfigurationForFront configurationForFront : configurationForFronts) {
            for (ConfigurationForFront.ConfigurationWrapper configurationWrapper : configurationForFront.getConfigurationWrappers()) {
                Configuration configuration = new Configuration();
                configuration.setFun_code(configurationWrapper.getFun_code());
                configuration.setProtocolId(Common.PROTOCOL_STR_TO_INT.inverse().get(configurationForFront.getProtocol()));
                list.add(configuration);
            }
        }
        return list;
    }
}
