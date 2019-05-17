package com.zjucsc.application.system.controller;


import com.zjucsc.application.domain.bean.*;
import com.zjucsc.application.domain.exceptions.ProtocolIdNotValidException;
import com.zjucsc.application.system.entity.ConfigurationSetting;
import com.zjucsc.application.system.service.iservice.IConfigurationSettingService;
import com.zjucsc.application.system.service.iservice.IProtocolIdService;
import com.zjucsc.application.util.CommonCacheUtil;
import com.zjucsc.base.BaseResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;

import static com.zjucsc.application.util.CommonCacheUtil.*;
import static com.zjucsc.application.util.CommonCacheUtil.addNewProtocolToCache;

/**
 * @author hongqianhui
 */
@RestController
@RequestMapping("/rule")
public class ConfigurationSettingController {

    @Autowired private IConfigurationSettingService iConfigurationSettingService;
    @Autowired private IProtocolIdService iProtocolIdService;


    @ApiOperation(value = "添加新的协议，返回该协议对应的ID")
    @PostMapping(value = "/new_protocol")
    public BaseResponse addNewProtocol(@RequestBody @Valid @NotNull ConfigurationForNewProtocol configurationForFronts) throws ProtocolIdNotValidException {
        if (protocolExist(configurationForFronts.getProtocolName())){
            throw new ProtocolIdNotValidException(configurationForFronts.getProtocolName() + " 协议已经存在 ");
        }
        addNewProtocolToCacheFirst(configurationForFronts);           //缓存中是不存在该协议的，因此需要先将生成的协议ID缓存到STRING--INT转换表中
        List<ConfigurationSetting> list = convertFrontAddingToEntity(configurationForFronts);
        if (list.size() > 0) {
            iConfigurationSettingService.saveOrUpdateBatch(list);
        }
        for (ConfigurationSetting configurationSetting : list) {

        }
        return BaseResponse.OK(convertNameToId(configurationForFronts.getProtocolName()));
    }

    private void addNewProtocolToCacheFirst(ConfigurationForNewProtocol configurationForFronts) throws ProtocolIdNotValidException {
        String protocolName = configurationForFronts.getProtocolName();
        com.zjucsc.application.system.entity.ProtocolId protocolIdAndName = new com.zjucsc.application.system.entity.ProtocolId();
        protocolIdAndName.setProtocolName(protocolName);
        protocolIdAndName.setProtocolId(iProtocolIdService.getMax());

        iProtocolIdService.save(protocolIdAndName);
        int newProtocolId = protocolIdAndName.getProtocolId();  //新增协议对应的协议ID
        addNewProtocolToCache(protocolName , newProtocolId);    //将新增的协议添加到两个缓存中
    }

    @ApiOperation(value = "新增协议的功能码")
    @PostMapping(value = "/new_funcode")
    public BaseResponse newConfigurationFuncode(@RequestBody @Valid  ConfigurationForFront configuration) throws ProtocolIdNotValidException {
        List<ConfigurationSetting> list = convertFrontUpdateToEntity(Collections.singletonList(configuration));
        iConfigurationSettingService.saveBatch(list);
        return BaseResponse.OK();
    }

    @ApiOperation(value = "更新协议的功能码")
    @PostMapping(value = "/update_funcode")
    public BaseResponse updateConfigurationFuncode(@RequestBody @Valid ConfigurationForFront configuration) throws ProtocolIdNotValidException {
        for (ConfigurationForFront.ConfigurationWrapper configurationWrapper : configuration.getConfigurationWrappers()) {
//            ConfigurationSetting configurationSetting = new ConfigurationSetting();
//            configurationSetting.setOpt(configurationWrapper.getOpt());
//            QueryWrapper<ConfigurationSetting> wrapper = new QueryWrapper<>(configurationSetting);
//            wrapper.eq("protocol_id" , configuration.getProtocolId());
//            wrapper.eq("fun_code" , configurationWrapper.getFun_code());
            iConfigurationSettingService.updateFuncode(configuration.getProtocolId(),configurationWrapper.getFun_code(),configurationWrapper.getOpt());
            updateOldProtocolCacheById(configuration.getProtocolId(),configurationWrapper.getFun_code(),configurationWrapper.getOpt());
        }
        return BaseResponse.OK();
    }

    @ApiOperation(value = "删除功能码")
    @DeleteMapping(value = "/deletecode")
    public BaseResponse deleteConfiguration(@RequestBody @Valid @NotEmpty List<ConfigurationForDelete> configurationForDeletes) throws ProtocolIdNotValidException {
        List<ConfigurationSetting> list = convertFrontToEntity(configurationForDeletes);
        HashMap<String,Object> map = new HashMap<>();
        for (ConfigurationSetting configuration : list) {
            map.clear();
            map.put("protocol_id" , configuration.getProtocolId());
            map.put("fun_code" , configuration.getFunCode());
            iConfigurationSettingService.removeByMap(map);
            deleteCachedFuncodeById(configuration.getProtocolId() , configuration.getFunCode());
        }
        return BaseResponse.OK();
    }

    private List<ConfigurationSetting> convertFrontToEntity(List<ConfigurationForDelete> configurationForFronts) throws ProtocolIdNotValidException {
        List<ConfigurationSetting> list = new ArrayList<>();
        for (ConfigurationForDelete configurationForDelete : configurationForFronts) {
            for (int fun_code : configurationForDelete.getFunCodes()) {
                ConfigurationSetting configuration = new ConfigurationSetting();
                configuration.setFunCode(fun_code);
                configuration.setProtocolId(configurationForDelete.getProtocolId());
                list.add(configuration);
            }
        }
        return list;
    }

    private List<ConfigurationSetting> convertFrontAddingToEntity(ConfigurationForNewProtocol configurationForNewProtocol) throws ProtocolIdNotValidException {
        List<ConfigurationSetting> list = new ArrayList<>();
        for (ConfigurationForFront.ConfigurationWrapper configurationWrapper : configurationForNewProtocol.getConfigurationWrappers()) {
            ConfigurationSetting configuration = new ConfigurationSetting();
            configuration.setFunCode(configurationWrapper.getFun_code());
            configuration.setProtocolId(convertNameToId(configurationForNewProtocol.getProtocolName()));
            configuration.setOpt(configurationWrapper.getOpt());
            //更新已有的功能码表
            updateOldProtocolCacheByName(configurationForNewProtocol.getProtocolName() ,
                        configurationWrapper.getFun_code(),configurationWrapper.getOpt());
            list.add(configuration);
        }
        return list;
    }

    private List<ConfigurationSetting> convertFrontUpdateToEntity(List<ConfigurationForFront> configurationForFronts) throws ProtocolIdNotValidException {
        List<ConfigurationSetting> list = new ArrayList<>();
        for (ConfigurationForFront configurationForFront : configurationForFronts) {
            // protocol
            for (ConfigurationForFront.ConfigurationWrapper configurationWrapper : configurationForFront.getConfigurationWrappers()) {
                //fun code
                ConfigurationSetting configuration = new ConfigurationSetting();
                configuration.setFunCode(configurationWrapper.getFun_code());
                configuration.setProtocolId(configurationForFront.getProtocolId());
                configuration.setOpt(configurationWrapper.getOpt());
                //更新已有的功能码表
                updateOldProtocolCacheById(configurationForFront.getProtocolId(),configurationWrapper.getFun_code(),
                        configurationWrapper.getOpt());
                list.add(configuration);
            }
        }
        return list;
    }

    /**
     * 模糊查询功能码表
     * @param configurationForSelect
     * @return
     */
    @ApiOperation("功能码含义表查询")
    @PostMapping("/funcode_list")
    public BaseResponse selectConfigurationPageInfo(@RequestBody @Valid ConfigurationForSelect configurationForSelect){
        List<ConfigurationWrapper> configurationWrappers = new ArrayList<>();
        for (ConfigurationSetting configuration : iConfigurationSettingService.selectConfigurationInfo(configurationForSelect)) {
            ConfigurationWrapper configurationWrapper = new ConfigurationWrapper(configuration.getFunCode() , configuration.getOpt());
            configurationWrappers.add(configurationWrapper);
        }
        ConfigurationForSelectCount count = new ConfigurationForSelectCount();
        count.setProtocolId(configurationForSelect.getProtocolId());
        count.setCodeDes(configurationForSelect.getCodeDes());
        int count_Res = (int) getConfigurationSize(count).data;
        ConfigurationForRe re = new ConfigurationForRe(count_Res , configurationWrappers);
        return BaseResponse.OK(re);
    }

    @ApiOperation("协议ID获取")
    @GetMapping("/protocol_list")
    public BaseResponse selectAllProtocolInfo(){
        List<com.zjucsc.application.domain.bean.ProtocolId> protocolIds = new ArrayList<>();
        for (com.zjucsc.application.system.entity.ProtocolId protocolId : iProtocolIdService.list()) {
            if (protocolId.getProtocolId() < 0){
                continue;
            }
            protocolIds.add(new com.zjucsc.application.domain.bean.ProtocolId(protocolId.getProtocolId(),protocolId.getProtocolName()));
        }
        return BaseResponse.OK(protocolIds);
    }

    public BaseResponse getConfigurationSize(ConfigurationForSelectCount configurationForSelectCount){
        return BaseResponse.OK(iConfigurationSettingService.selectConfigurationCount(
                configurationForSelectCount.getCodeDes(),
                configurationForSelectCount.getProtocolId()
        ));
    }

    @ApiOperation("删除协议")
    @DeleteMapping("delete_protocol")
    public BaseResponse deleteProtocol(@RequestParam int protocolId) throws ProtocolIdNotValidException {
        Map<String,Object> map = new HashMap<>();
        map.put("protocol_id" , protocolId);
        iConfigurationSettingService.removeByMap(map);
        deleteCachedProtocolByID(protocolId);
        iProtocolIdService.removeByMap(map);
        return BaseResponse.OK();
    }

}
