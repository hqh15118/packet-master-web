package com.zjucsc.application.controller;

import com.zjucsc.application.config.Common;
import com.zjucsc.application.domain.bean.ArtArgShowState;
import com.zjucsc.application.domain.bean.ArtConfig;
import com.zjucsc.application.domain.bean.ArtConfigPaged;
import com.zjucsc.application.system.service.hessian_iservice.IArtConfigService;
import com.zjucsc.application.util.CommonCacheUtil;
import com.zjucsc.application.util.AppCommonUtil;
import com.zjucsc.art_decode.ArtDecodeCommon;
import com.zjucsc.art_decode.artconfig.ModBusConfig;
import com.zjucsc.application.domain.bean.BaseResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author hongqianhui
 */
@RestController
@RequestMapping("/art_config/")
public class ArtConfigController {
    @Autowired
    private IArtConfigService iArtConfigService;

    @ApiOperation("添加/更新工艺参数配置，artConfig > 0表示更新；不填表示添加新的配置，顺序返回记录的ID列表")
    @PostMapping("new_art_config1")
    public BaseResponse addOrUpdateArtConfig(@RequestBody @Valid List<ArtConfig> artConfig) {
        //更新数据库
        List<Integer> artConfigId = new ArrayList<>();
        List<ArtConfig> errorArtConfig = new ArrayList<>();
        for (ArtConfig config : artConfig) {
            if (!artConfigValid(config)){//判断工艺参数配置是否合法
                errorArtConfig.add(config);
                continue;
            }
            if (config.getArtConfigId() > 0) {
                //更新已有
                iArtConfigService.updateById(config);
            }else{
                //添加新的工艺参数配置到数据库
                iArtConfigService.insertById(config);
                //初始化工艺参数配置
                //将该工艺参数添加到MAP中
                AppCommonUtil.initArtMap(config.getTag());
            }
            artConfigId.add(config.getArtConfigId());
            if (config.isShowGraph()){
                CommonCacheUtil.addShowGraphArg(config.getProtocolId(),config.getTag());
            }else{
                CommonCacheUtil.removeShowGraph(config.getProtocolId(),config.getTag());
            }
        }
        //更新缓存
        artConfig.remove(errorArtConfig);       //移除所有不合法的配置
        CommonCacheUtil.addOrUpdateDecodeInstances(artConfig);  //合法配置添加到缓存
        if (errorArtConfig.size() == 0)
            return BaseResponse.OK(artConfigId);
        else
            return BaseResponse.ERROR(Common.HTTP_STATUS_CODE.ART_CONFIG_NOT_VALID,"工艺参数不合法：\n" + errorArtConfig);
    }

    private boolean artConfigValid(ArtConfig artConfig){
        // + 1 是因为offset是从0开始的
        return (artConfig.getOffset() + artConfig.getLength() + 1) >= artConfig.getMinLength();
    }

    @ApiOperation("根据ID删除工艺参数配置")
    @PostMapping("delete_art_config")
    public BaseResponse deleteArtConfig(@RequestBody List<Integer> artConfigId){
        ArtConfig artConfig =  null;
        for (Integer integer : artConfigId) {
            //移除缓存
            artConfig = iArtConfigService.getById(integer);
            CommonCacheUtil.removeArtDecodeInstanceByProtocolAndId(artConfig.getProtocolId(),integer);
            AppCommonUtil.removeArtMap(artConfig.getTag());
            //移除数据库
            iArtConfigService.deleteById(integer);
        }
        return BaseResponse.OK();
    }

    @ApiOperation("获取所有工艺参数配置")
    @GetMapping("get_all_art_config")
    public BaseResponse getAllArtConfig(){
        return BaseResponse.OK(iArtConfigService.selectAllConfig());
    }

    @ApiOperation("分页获取工艺参数配置，可以选择协议和minLength")
    @PostMapping("get_paged_art_config")
    public BaseResponse getPagedArtConfig(@RequestBody ArtConfigPaged artConfigPaged){
        return BaseResponse.OK(iArtConfigService.getConfigPaged(artConfigPaged));
    }

    @ApiOperation("修改工艺参数显示状态，【取消显示】时返回取消状态，true表示显示列表中存在，false表示显示列表中不存在")
    @PostMapping("change_show_state")
    public BaseResponse changeArtShowState(@RequestBody ArtArgShowState artArgShowState){
        if (artArgShowState.isShowState()){
            //如果要显示
            CommonCacheUtil.addShowGraphArg(artArgShowState.getProtocolId(),artArgShowState.getArtName());
            return BaseResponse.OK(true);
        }else{
            return BaseResponse.OK(CommonCacheUtil.removeShowGraph(artArgShowState.getProtocolId(),artArgShowState.getArtName()));
        }
    }

    @ApiOperation("添加工艺参数脚本")
    @PostMapping("upload_script")
    public BaseResponse uploadScript(MultipartFile scriptFile){
        try {
            CompletableFuture<Exception> completeFuture = iArtConfigService.saveScriptFile(scriptFile.getInputStream(),scriptFile.getOriginalFilename());
            if (completeFuture.get()==null){
                return BaseResponse.OK();
            }else{
                return BaseResponse.ERROR(Common.HTTP_STATUS_CODE.SCRIPT_UP_LOAD_FAIL,"脚本文件上传失败");
            }
        } catch (IOException | InterruptedException | ExecutionException e) {
            return BaseResponse.ERROR(Common.HTTP_STATUS_CODE.SCRIPT_UP_LOAD_FAIL,"脚本文件上传失败");
        }
    }

    @ApiOperation("判断制定脚本文件是否存在")
    @GetMapping("script_exist")
    public BaseResponse checkScriptExistOrNot(@RequestParam String scriptName){
        return BaseResponse.OK(iArtConfigService.scriptExistOrNot(scriptName));
    }

    @ApiOperation("删除MODBUS工艺参数配置")
    @PostMapping("del_art_config_modbus")
    public BaseResponse delModbusArtConfig(@RequestBody ModBusConfig modBusConfig){
        boolean b = ArtDecodeCommon.deleteArtConfig(modBusConfig);
        return b?BaseResponse.OK() : BaseResponse.ERROR(Common.HTTP_STATUS_CODE.ART_DELETE_FAIL,"modbus工艺参数删除失败");
    }

    @ApiOperation("添加MODBUS工艺参数配置")
    @PostMapping("new_art_config_modbus")
    public BaseResponse addOrUpdateModbusArtConfig(@RequestBody ModBusConfig modBusConfig){
        boolean b = ArtDecodeCommon.addArtDecodeConfig(modBusConfig);
        return b?BaseResponse.OK() : BaseResponse.ERROR(Common.HTTP_STATUS_CODE.ART_DELETE_FAIL,"modbus工艺参数添加失败");
    }
}
