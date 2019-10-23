package com.zjucsc.application.controller.artcontroller;

import com.alibaba.fastjson.JSON;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.config.PACKET_PROTOCOL;
import com.zjucsc.application.config.Refactor;
import com.zjucsc.application.config.auth.Log;
import com.zjucsc.application.domain.bean.ArtArgShowState;
import com.zjucsc.application.domain.bean.BaseArtConfig;
import com.zjucsc.application.domain.bean.BaseResponse;
import com.zjucsc.application.domain.bean.PagedArtConfig;
import com.zjucsc.application.statistic.StatisticsData;
import com.zjucsc.application.system.service.hessian_iservice.IArtConfigService;
import com.zjucsc.application.util.CacheUtil;
import com.zjucsc.art_decode.ArtDecodeUtil;
import com.zjucsc.art_decode.artconfig.*;
import com.zjucsc.art_decode.base.BaseConfig;
import com.zjucsc.common.exceptions.ProtocolIdNotValidException;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author hongqianhui
 */
@RestController
@RequestMapping("/art_config/")
@Slf4j
public class ArtConfigController {

    @Autowired private IArtConfigService iArtConfigService;


    @ApiOperation("添加工艺参数配置，artConfig > 0表示更新；不填表示添加新的配置，顺序返回记录的ID列表")
    @PostMapping("new_config")
    @Refactor("20191023")
    public BaseResponse addOrUpdateArtConfig(HttpServletRequest request) {
        String jsonData = getJSONDataFromRequest(request);
        BaseConfig baseConfig = JSON.parseObject(jsonData,BaseConfig.class);

        //更新数据库
        if (baseConfig.getId() > 0) {
            //更新已有
             iArtConfigService.updateByJSONStr(jsonData);
        }else{
            //添加新的工艺参数配置到数据库
            iArtConfigService.insertByJSONStr(jsonData);
            //初始化工艺参数配置
            //将该工艺参数添加到MAP中，包括前端推送的map和后端内存存储put的map
            StatisticsData.initArtMap(baseConfig.getTag());
        }
        //更新缓存
        return iArtConfigService.setArtConfig(baseConfig.getProtocolId(),
                                              jsonData, baseConfig);
    }

    @ApiOperation("根据ID删除工艺参数配置")
    @Log
    @DeleteMapping("delete_art_config")
    /**
     * 1.前端不显示；（不推送）
     * 2.后端删除配置；
     * 3.GLOBAL MAP删除；
     * 4.工艺参数组别删除；
     */
    public BaseResponse deleteArtConfig(@RequestParam  int artConfigId , @RequestParam int protocolId) throws ProtocolIdNotValidException {
        BaseArtConfig baseArtConfig = iArtConfigService.getArtConfigByProtocolIdAndId(protocolId, artConfigId);

        BaseConfig baseConfig = ((BaseConfig) baseArtConfig.getConfigStructure());
        if (baseConfig.getProtocolId() == 2){
            baseConfig.setProtocol("s7comm");
        }else{
            baseConfig.setProtocol(CacheUtil.convertIdToName(baseConfig.getProtocolId()));
        }
        //移除要显示的map和存储的map
        StatisticsData.removeArtMap2ShowAndSave(baseConfig.getTag());
        //移除解析库中的配置
        ArtDecodeUtil.deleteArtConfig(baseConfig);
        //移除工艺参数组别中的工艺参数
        CacheUtil.removeArtNameInGroup(baseConfig.getTag());
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
        List configList = (List) (iArtConfigService.getConfigPaged(pagedArtConfig).data);
        if (pagedArtConfig.getProtocolId() == PACKET_PROTOCOL.MODBUS_ID){
            addNewTag(configList,ModBusConfig.class);
        }
        if (pagedArtConfig.getProtocolId() == PACKET_PROTOCOL.S7_ID){
            addNewTag(configList, S7Config.class);
        }
        return BaseResponse.OK(configList);
    }

    private void addNewTag(List configList,Class<? extends BaseConfig> targetClass){
        for (Object o : configList) {
            BaseConfig baseConfig = targetClass.cast(o);
            StatisticsData.initArtMap(baseConfig.getTag());
            if (baseConfig.getShowGraph() == 1){
                CacheUtil.addShowGraphArg(baseConfig.getProtocolId(),baseConfig.getTag());
            }
        }
    }

    @ApiOperation("修改工艺参数显示状态，【取消显示】时返回取消状态，true表示显示列表中存在，false表示显示列表中不存在")
    @PostMapping("change_show_state")
    @Log
    public BaseResponse changeArtShowState(@RequestBody ArtArgShowState artArgShowState){
        iArtConfigService.changeArtConfigShowState(artArgShowState);
        if (artArgShowState.getShowGraph() == 1){
            //如果要显示
            CacheUtil.addShowGraphArg(artArgShowState.getProtocolId(),artArgShowState.getTag());
            return BaseResponse.OK(true);
        }else{
            return BaseResponse.OK(CacheUtil.removeShowGraph(artArgShowState.getProtocolId(),artArgShowState.getTag()));
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

    @ApiOperation("获取特定协议下的所有工艺参数")
    @GetMapping("protocol_art")
    @Log
    public BaseResponse getAllArtConfigOfTargetProtocol(@RequestParam String protocol){
        return BaseResponse.OK(iArtConfigService.selectArtNameByProtocolName(protocol));
    }



    private String getJSONDataFromRequest(HttpServletRequest request){
        StringBuilder sb = new StringBuilder();
        try(BufferedReader bfr = request.getReader()){
            String str;
            while ((str = bfr.readLine())!=null){
                sb.append(str);
            }
        } catch (IOException e) {
            log.error("无法读取JSON数据");
        }
        return sb.toString();
    }

}
