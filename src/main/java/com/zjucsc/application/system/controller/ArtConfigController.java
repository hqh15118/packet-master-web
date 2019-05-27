package com.zjucsc.application.system.controller;

import com.zjucsc.application.domain.bean.ArtConfigPaged;
import com.zjucsc.application.system.entity.ArtConfig;
import com.zjucsc.application.system.service.iservice.IArtConfigService;
import com.zjucsc.base.BaseResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hongqianhui
 */
@RestController
@RequestMapping("/art-config/")
public class ArtConfigController {
    @Autowired
    private IArtConfigService iArtConfigService;

    @ApiOperation("添加/更新工艺参数配置，artConfig > 0表示更新；不填表示添加新的配置")
    @PostMapping("/new_art_config")
    public BaseResponse addOrUpdateArtConfig(@RequestBody List<ArtConfig> artConfig){
        List<Integer> artConfigId = new ArrayList<>();
        for (ArtConfig config : artConfig) {
            if (config.getArtConfigId() > 0) {
                //更新已有
                iArtConfigService.updateById(config);
            }else{
                //添加新的工艺参数配置
                iArtConfigService.save(config);
            }
            artConfigId.add(config.getArtConfigId());
        }
        return BaseResponse.OK(artConfigId);
    }

    @ApiOperation("根据ID删除工艺参数配置")
    @PostMapping("/delete_art_config")
    public BaseResponse deleteArtConfig(@RequestBody List<Integer> artConfigId){
        boolean t = true;
        for (Integer integer : artConfigId) {
            t = t && iArtConfigService.removeById(integer);
        }
        return BaseResponse.OK(t);
    }

    @ApiOperation("获取所有工艺参数配置")
    @GetMapping("get_all_art_config")
    public BaseResponse getAllArtConfig(){
        return BaseResponse.OK(iArtConfigService.list());
    }

    @ApiOperation("分页获取工艺参数配置，可以选择协议和minLength")
    @PostMapping("get_paged_art_config")
    public BaseResponse getPagedArtConfig(@RequestBody ArtConfigPaged artConfigPaged){
        return BaseResponse.OK(iArtConfigService.getConfigPaged(artConfigPaged));
    }
}
