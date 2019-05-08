package com.zjucsc.application.system.controller;


import com.zjucsc.application.config.Common;
import com.zjucsc.application.domain.bean.*;
import com.zjucsc.application.domain.exceptions.ProtocolIdNotValidException;
import com.zjucsc.application.system.entity.Configuration;
import com.zjucsc.application.system.entity.ProtocolId;
import com.zjucsc.application.system.service.IProtocolIdService;
import com.zjucsc.application.system.service.iservice.ConfigurationService;
import com.zjucsc.application.util.CommonUtil;
import com.zjucsc.base.BaseResponse;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/configuration")
public class ConfigurationController {

    @Autowired private ConfigurationService configurationService;
    @Autowired private IProtocolIdService iProtocolIdService;


    @ApiOperation(value = "添加新的协议，返回该协议对应的ID")
    @PostMapping(value = "/add_protocol")
    public BaseResponse addConfiguration(@RequestBody @Valid @NotEmpty List<ConfigurationForNewProtocol> configurationForFronts){
        List<Configuration> list = convertFrontAddingToEntity(configurationForFronts);
        for (Configuration configuration : list) {
            Common.CONFIGURATION_MAP.get(Common.PROTOCOL_STR_TO_INT.get(configuration.getProtocol_id())).
                put(configuration.getFun_code(), configuration.getOpt());
        }
        configurationService.saveOrUpdateBatch(list);
        return BaseResponse.OK(list.get(0).getProtocol_id());
    }

    @ApiOperation(value = "更新已有协议的功能码")
    @PostMapping(value = "/update_fun_code")
    public BaseResponse updateConfiguration(@RequestBody @Valid @NotEmpty List<ConfigurationForFront> configurationForFronts){
        MsgWrapper msgWrapper = convertFrontUpdateToEntity(configurationForFronts);
        configurationService.saveOrUpdateBatch(msgWrapper.getConfigurations());
        return BaseResponse.OK(msgWrapper.getErrorMsg());
    }

    @ApiOperation(value = "删除功能码")
    @PostMapping(value = "/delete_opt")
    public BaseResponse deleteConfiguration(@RequestBody @Valid @NotEmpty List<ConfigurationForDelete> configurationForDeletes) throws ProtocolIdNotValidException {
        List<Configuration> list = convertFrontToEntity(configurationForDeletes);
        HashMap<String,Object> map = new HashMap<>();
        for (Configuration configuration : list) {
            map.clear();
            map.put("protocol_id" , configuration.getProtocol_id());
            map.put("fun_code" , configuration.getFun_code());
            configurationService.removeByMap(map);
            CommonUtil.deleteCachedFuncodeById(configuration.getProtocol_id() , configuration.getFun_code());
        }
        return BaseResponse.OK();
    }

    private List<Configuration> convertFrontToEntity(List<ConfigurationForDelete> configurationForFronts) throws ProtocolIdNotValidException {
        List<Configuration> list = new ArrayList<>();
        for (ConfigurationForDelete configurationForDelete : configurationForFronts) {
            for (int fun_code : configurationForDelete.getFunCodes()) {
                Configuration configuration = new Configuration();
                configuration.setFun_code(fun_code);
                configuration.setProtocol_id(CommonUtil.convertNameToId(configurationForDelete.getProtocolName()));
                list.add(configuration);
            }
        }
        return list;
    }

    private StringBuilder appendErrorMsg(int protocolId , StringBuilder stringBuilder){
        if (stringBuilder == null){
            stringBuilder = new StringBuilder();
            stringBuilder.append("can not find protocol : ");
        }
        stringBuilder.append(protocolId).append( " ");
        return stringBuilder;
    }

    @Data
    private static class MsgWrapper{
        private List<Configuration> configurations;
        private String errorMsg;

        public MsgWrapper(List<Configuration> configurations, String errorMsg) {
            this.configurations = configurations;
            this.errorMsg = errorMsg;
        }
    }

    private List<Configuration> convertFrontAddingToEntity(List<ConfigurationForNewProtocol> configurationForFronts){
        List<Configuration> list = new ArrayList<>();
        for (ConfigurationForNewProtocol configurationForNewProtocol : configurationForFronts) {
            for (ConfigurationForFront.ConfigurationWrapper configurationWrapper : configurationForNewProtocol.getConfigurationWrappers()) {
                HashMap<Integer,String> mapFuncodeToOptByProtocol = Common.CONFIGURATION_MAP.get
                        (Common.PROTOCOL_STR_TO_INT.get(Common.PROTOCOL_STR_TO_INT.inverse().get(configurationForNewProtocol.getProtocolName())));
                if (mapFuncodeToOptByProtocol == null){
                    //未添加该协议
                    mapFuncodeToOptByProtocol = new HashMap<>();
                    Common.CONFIGURATION_MAP.put(configurationForNewProtocol.getProtocolName() , mapFuncodeToOptByProtocol);
                    Common.PROTOCOL_STR_TO_INT.put(Common.PROTOCOL_STR_TO_INT.size() + 1, configurationForNewProtocol.getProtocolName());
                }
                Configuration configuration = new Configuration();
                configuration.setFun_code(configurationWrapper.getFun_code());
                configuration.setProtocol_id(Common.PROTOCOL_STR_TO_INT.inverse().get(configurationForNewProtocol.getProtocolName()));
                mapFuncodeToOptByProtocol.put(configurationWrapper.getFun_code(),configurationWrapper.getOpt());
                configuration.setOpt(configurationWrapper.getOpt());
                list.add(configuration);
            }
        }
        return list;
    }

    private MsgWrapper convertFrontUpdateToEntity(List<ConfigurationForFront> configurationForFronts){
        List<Configuration> list = new ArrayList<>();
        StringBuilder sb = null;
        for (ConfigurationForFront configurationForFront : configurationForFronts) {
            // protocol
            for (ConfigurationForFront.ConfigurationWrapper configurationWrapper : configurationForFront.getConfigurationWrappers()) {
                //fun code
                //获取该协议对应的功能码表
                HashMap<Integer,String> mapFuncodeToOptByProtocol = Common.CONFIGURATION_MAP.get
                        (Common.PROTOCOL_STR_TO_INT.get(configurationForFront.getProtocolId()));
                System.out.println(Common.CONFIGURATION_MAP);
                System.out.println(Common.PROTOCOL_STR_TO_INT);
                if (mapFuncodeToOptByProtocol == null){
                    //未添加该协议
                    sb = appendErrorMsg(configurationForFront.getProtocolId() , sb);
                    continue;
                }
                Configuration configuration = new Configuration();
                configuration.setFun_code(configurationWrapper.getFun_code());
                configuration.setProtocol_id(configurationForFront.getProtocolId());
                //更新已有的功能码表
                mapFuncodeToOptByProtocol.put(configurationWrapper.getFun_code(),configurationWrapper.getOpt());
                configuration.setOpt(configurationWrapper.getOpt());
                list.add(configuration);
            }
        }
        return sb == null ? new MsgWrapper(list , null) : new MsgWrapper(list , sb.toString());
    }


    @ApiOperation("查询功能码含义")
    @GetMapping("/get_configuration_info")
    public BaseResponse selectConfigurationPageInfo(@RequestBody @Valid ConfigurationForSelect configurationForSelect){
        List<ConfigurationWrapper> configurationWrappers = new ArrayList<>();
        for (Configuration configuration : configurationService.selectConfigurationInfo(configurationForSelect)) {
            ConfigurationWrapper configurationWrapper = new ConfigurationWrapper(configuration.getFun_code() , configuration.getOpt());
            configurationWrappers.add(configurationWrapper);
        }
        return BaseResponse.OK(configurationWrappers);
    }

    @ApiOperation("查询所有协议含义")
    @GetMapping("/get_all_protocol_info")
    public BaseResponse selectAllProtocolInfo(){
        List<com.zjucsc.application.domain.bean.ProtocolId> protocolIds = new ArrayList<>();
        for (ProtocolId protocolId : iProtocolIdService.list()) {
            if (protocolId.getProtocolId() < 0){
                continue;
            }
            protocolIds.add(new com.zjucsc.application.domain.bean.ProtocolId(protocolId.getProtocolId(),protocolId.getProtocolName()));
        }
        return BaseResponse.OK(protocolIds);
    }

    @ApiOperation("查询所有功能码条数")
    @GetMapping("/get_configuration_size")
    public BaseResponse getConfigurationSize(@RequestBody ConfigurationForSelect configurationForSelect){
        return BaseResponse.OK(configurationService.selectConfigurationCount(configurationForSelect));
    }


}
