package com.zjucsc.application.controller;


import com.zjucsc.application.config.auth.Log;
import com.zjucsc.application.domain.bean.*;
import com.zjucsc.application.domain.exceptions.ProtocolIdNotValidException;
import com.zjucsc.application.system.service.hessian_iservice.IConfigurationSettingService;
import com.zjucsc.application.system.service.hessian_iservice.IProtocolIdService;
import com.zjucsc.application.util.CommonCacheUtil;
import com.zjucsc.application.domain.bean.BaseResponse;
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


    @Log
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
            //更新到缓存
            HashMap<Integer,String> funCodeMeaningMap = new HashMap<>();
            for (ConfigurationSetting configurationSetting : list) {
                funCodeMeaningMap.put(configurationSetting.getFunCode(),configurationSetting.getOpt());
            }
            CommonCacheUtil.addNewProtocolAndFuncodeMapToCache(configurationForFronts.getProtocolName(),
                    convertNameToId(configurationForFronts.getProtocolName()),
                    funCodeMeaningMap);
        }
        return BaseResponse.OK(convertNameToId(configurationForFronts.getProtocolName()));
    }

    private void addNewProtocolToCacheFirst(ConfigurationForNewProtocol configurationForFronts) throws ProtocolIdNotValidException {
        String protocolName = configurationForFronts.getProtocolName();
        Protocol protocolAndName = new Protocol();
        protocolAndName.setProtocolName(protocolName);
        iProtocolIdService.insertById(protocolAndName);
        protocolAndName.setProtocolId((Integer) protocolAndName.data);
        int newProtocolId = protocolAndName.getProtocolId();  //新增协议对应的协议ID
        addNewProtocolToCache(protocolName , newProtocolId);    //将新增的协议添加到两个缓存中
    }

    @Log
    @ApiOperation(value = "新增协议的功能码")
    @PostMapping(value = "/new_funcode")
    public BaseResponse newConfigurationFuncode(@RequestBody @Valid  ConfigurationForFront configuration) throws ProtocolIdNotValidException {
        List<ConfigurationSetting> list = convertFrontUpdateToEntity(Collections.singletonList(configuration));
        iConfigurationSettingService.saveOrUpdateBatch(list);
        return BaseResponse.OK();
    }

    @Log
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

    @Log
    @ApiOperation(value = "删除功能码")
    @DeleteMapping(value = "/deletecode")
    public BaseResponse deleteConfiguration(@RequestBody @Valid @NotEmpty List<ConfigurationForDelete> configurationForDeletes) throws ProtocolIdNotValidException {
        List<ConfigurationSetting> list = convertFrontToEntity(configurationForDeletes);
        for (ConfigurationSetting configuration : list) {
            iConfigurationSettingService.deleteByProtocolIdAndFuncode(configuration.getProtocolId(),configuration.getFunCode());
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
    @Log
    @ApiOperation("功能码含义表查询")
    @PostMapping("/funcode_list")
    public BaseResponse selectConfigurationPageInfo(@RequestBody @Valid ConfigurationForSelect configurationForSelect){
        return BaseResponse.OK(getConfigurationRe(configurationForSelect));
    }

    private ConfigurationForReturn getConfigurationRe(ConfigurationForSelect configurationForSelect){
        List<ConfigurationWrapper> configurationWrappers = new ArrayList<>();
        for (ConfigurationSetting configuration : iConfigurationSettingService.selectConfigurationInfo(configurationForSelect)) {
            ConfigurationWrapper configurationWrapper = new ConfigurationWrapper(configuration.getFunCode() , configuration.getOpt());
            configurationWrappers.add(configurationWrapper);
        }
        ConfigurationForSelectCount count = new ConfigurationForSelectCount();
        count.setProtocolId(configurationForSelect.getProtocolId());
        count.setCodeDes(configurationForSelect.getCodeDes());
        int count_Res = (int) getConfigurationSize(count).data;
        return new ConfigurationForReturn(count_Res , configurationWrappers);
    }

    @Log
    @ApiOperation("协议ID获取")
    @GetMapping("/protocol_list")
    public BaseResponse selectAllProtocolInfo(){
        List<Protocol> protocols = new ArrayList<>();
        for (Protocol protocol : iProtocolIdService.selectAll()) {
            if (protocol.getProtocolId() < 0){
                continue;
            }
            protocols.add(new Protocol(protocol.getProtocolId(), protocol.getProtocolName()));
        }
        return BaseResponse.OK(protocols);
    }

    public BaseResponse getConfigurationSize(ConfigurationForSelectCount configurationForSelectCount){
        return BaseResponse.OK(iConfigurationSettingService.selectConfigurationCount(
                configurationForSelectCount.getCodeDes(),
                configurationForSelectCount.getProtocolId()
        ));
    }

    @Log
    @ApiOperation("删除协议")
    @DeleteMapping("delete_protocol")
    public BaseResponse deleteProtocol(@RequestParam int protocolId) throws ProtocolIdNotValidException {
        iConfigurationSettingService.deleteByProtocolId(protocolId);
        deleteCachedProtocolByID(protocolId);
        iProtocolIdService.deleteByProtocolId(protocolId);
        return BaseResponse.OK();
    }

    @Log
    @ApiOperation("查询指定协议列表下的所有功能码及含义")
    @PostMapping("protocol_lists")
    public BaseResponse selectConfigurationPageInfos(@RequestBody @NotEmpty @Valid List<ConfigurationForSelect> protocols){
        List<ConfigurationForReturn> list = new ArrayList<>();
        for (ConfigurationForSelect protocol : protocols) {
            list.add(getConfigurationRe(protocol));
        }
        return BaseResponse.OK(list);
    }

}
