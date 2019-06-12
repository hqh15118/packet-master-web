package com.zjucsc.application.controller;

import com.alibaba.fastjson.JSON;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.config.PACKET_PROTOCOL;
import com.zjucsc.application.config.auth.Log;
import com.zjucsc.application.domain.bean.*;
import com.zjucsc.application.domain.exceptions.ProtocolIdNotValidException;
import com.zjucsc.application.system.service.hessian_iservice.IArtConfigService;
import com.zjucsc.application.util.AppCommonUtil;
import com.zjucsc.application.util.CommonCacheUtil;
import com.zjucsc.art_decode.ArtDecodeCommon;
import com.zjucsc.art_decode.artconfig.BaseConfig;
import com.zjucsc.art_decode.artconfig.ModBusConfig;
import com.zjucsc.art_decode.artconfig.S7techpara;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
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

    @Autowired private IArtConfigService iArtConfigService;

    @ApiOperation("添加工艺参数配置，artConfig > 0表示更新；不填表示添加新的配置，顺序返回记录的ID列表")
    @PostMapping("new_config")
    public BaseResponse addOrUpdateArtConfig(HttpServletRequest request) throws ProtocolIdNotValidException, IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader bfr = request.getReader();
        String str;
        while ((str = bfr.readLine())!=null){
            sb.append(str);
        }
        String jsonData = sb.toString();
        BaseConfig baseConfig = JSON.parseObject(jsonData,BaseConfig.class);

        //更新数据库
        if (baseConfig.getId() > 0) {
            //更新已有
             iArtConfigService.updateByJSONStr(jsonData);
        }else{
            //添加新的工艺参数配置到数据库
            //iArtConfigService.insertByJSONStr(jsonData);
            //初始化工艺参数配置
            //将该工艺参数添加到MAP中
            AppCommonUtil.initArtMap(baseConfig.getTag());
        }

        if (baseConfig.getShowGraph() == 0){
            CommonCacheUtil.addShowGraphArg(baseConfig.getProtocolId(),baseConfig.getTag());
        }else{
            CommonCacheUtil.removeShowGraph(baseConfig.getProtocolId(),baseConfig.getTag());
        }

        if (baseConfig.getProtocolId() == PACKET_PROTOCOL.MODBUS_ID){
            ModBusConfig modBusConfig = JSON.parseObject(jsonData,ModBusConfig.class);
            modBusConfig.setProtocol(PACKET_PROTOCOL.MODBUS);
            ArtDecodeCommon.addArtDecodeConfig(modBusConfig);
            return BaseResponse.OK(true);
        }else if (baseConfig.getProtocolId() == PACKET_PROTOCOL.S7_ID){
            S7techpara s7techpara = JSON.parseObject(jsonData, S7techpara.class);
            s7techpara.setProtocol("s7comm");
            ArtDecodeCommon.addArtDecodeConfig(s7techpara);
            return BaseResponse.OK(true);
        }
        return BaseResponse.OK(false);
    }

    @ApiOperation("根据ID删除工艺参数配置")
    @Log
    @DeleteMapping("delete_art_config")
    public BaseResponse deleteArtConfig(@RequestParam  int artConfigId , @RequestParam  int protocolId){
        BaseArtConfig baseArtConfig;
        //获取协议ID和工艺参数ID对应的那个工艺参数数据
        //{
        //      protocolId : 协议ID
        //      data : 工艺参数配置结构
        // }
        baseArtConfig = iArtConfigService.getArtConfigByProtocolIdAndId(protocolId, artConfigId);

        BaseConfig baseConfig = ((BaseConfig) baseArtConfig.getConfigStructure());
        //移除要显示的map
        AppCommonUtil.removeArtMap(baseConfig.getTag());
        //移除解析库中的配置
        ArtDecodeCommon.deleteArtConfig(baseConfig);
        //移除数据库，返回被移除的工艺参数配置
        return BaseResponse.OK(iArtConfigService.delArtConfigByProtocolIdAndId(protocolId,artConfigId));
    }


    @ApiOperation("获取所有工艺参数配置")
    @Log
    @GetMapping("get_all_art_config")
    public BaseResponse getAllArtConfig(){
        return BaseResponse.OK(iArtConfigService.selectAllConfig());
    }

    @ApiOperation("分页获取工艺参数配置")
    @PostMapping("get_paged_art_config")
    @Log
    public BaseResponse getPagedArtConfig(@RequestBody PagedArtConfig pagedArtConfig){
        return BaseResponse.OK(iArtConfigService.getConfigPaged(pagedArtConfig).data);
    }

    @ApiOperation("修改工艺参数显示状态，【取消显示】时返回取消状态，true表示显示列表中存在，false表示显示列表中不存在")
    @PostMapping("change_show_state")
    @Log
    public BaseResponse changeArtShowState(@RequestBody ArtArgShowState artArgShowState){
        iArtConfigService.changeArtConfigShowState(artArgShowState);
        if (artArgShowState.getShowState() == 1){
            //如果要显示
            CommonCacheUtil.addShowGraphArg(artArgShowState.getProtocolId(),artArgShowState.getTag());
            return BaseResponse.OK(true);
        }else{
            return BaseResponse.OK(CommonCacheUtil.removeShowGraph(artArgShowState.getProtocolId(),artArgShowState.getTag()));
        }
    }

    @ApiOperation("添加工艺参数脚本")
    @PostMapping("upload_script")
    @Log
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
    @Log
    public BaseResponse checkScriptExistOrNot(@RequestParam String scriptName){
        return BaseResponse.OK(iArtConfigService.scriptExistOrNot(scriptName));
    }

    @ApiOperation("获取所有工艺参数显示状态")
    @GetMapping("show_states")
    @Log
    public BaseResponse getAllArtConfigShowState(){
        return BaseResponse.OK(iArtConfigService.selectAllArtConfigShowState());
    }
}
