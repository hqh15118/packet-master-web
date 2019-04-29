package com.zjucsc.application.system.controller;


import com.zjucsc.application.domain.entity.ConfigurationSetting;
import com.zjucsc.application.system.service.ConfigurationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/configuration")
public class ConfigurationController {

    @Autowired
    private ConfigurationService configurationService;

    @ApiOperation(value="添加组态")
    @RequestMapping(value = "/newsetting" , method = RequestMethod.POST)
    public void addConfigurationSetting(@RequestBody ConfigurationSetting configurationSetting){
        configurationService.save(configurationSetting);
    }
}
