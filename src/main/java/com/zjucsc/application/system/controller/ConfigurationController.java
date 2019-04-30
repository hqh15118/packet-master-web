package com.zjucsc.application.system.controller;


import com.zjucsc.application.config.Common;
import com.zjucsc.application.domain.entity.ConfigurationSetting;
import com.zjucsc.application.system.service.ConfigurationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/configuration")
public class ConfigurationController {

    @Autowired
    private ConfigurationService configurationService;

    @ApiOperation(value="添加组态")
    @RequestMapping(value = "/new_configuration" , method = RequestMethod.POST)
    public void addConfigurationSetting(@RequestBody @Valid ConfigurationSetting configurationSetting){

        configurationService.save(configurationSetting);
    }

    @ApiOperation(value = "查询所有组态")
    @GetMapping(value = "/get_configuration")
    public HashMap<String, Map<Integer,String>> getAllConfiguration(){
        return Common.CONFIGURATION_MAP;
    }
}
