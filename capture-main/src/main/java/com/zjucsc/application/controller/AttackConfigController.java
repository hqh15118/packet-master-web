package com.zjucsc.application.controller;


import com.zjucsc.application.config.Common;
import com.zjucsc.application.config.StatisticsData;
import com.zjucsc.application.config.auth.Log;
import com.zjucsc.application.domain.bean.*;
import com.zjucsc.application.domain.non_hessian.DeviceMaxFlow;
import com.zjucsc.application.system.service.hessian_mapper.PacketInfoMapper;
import com.zjucsc.attack.bean.ArtAttackAnalyzeConfig;
import com.zjucsc.attack.bean.AttackConfig;
import com.zjucsc.attack.common.AttackCommon;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hongqianhui
 */
@RestController
@RequestMapping("/attack")
public class AttackConfigController {

    @Autowired private PacketInfoMapper packetInfoMapper;
    /**
     * 配置一定时间内的最大流量，超过报警
     * @param flowInByte
     * @return
     */
    @ApiOperation("配置一定时间内的总的最大流量，超过报警")
    @GetMapping("max_flow")
    @Log
    public BaseResponse configMaxFlow(@RequestParam int flowInByte){
        Common.maxFlowInByte = flowInByte;
        return BaseResponse.OK();
    }

    @ApiOperation("配置一定时间内的设备的最大流量，超过报警")
    @PostMapping("device_max_flow")
    @Log
    public BaseResponse configMaxFlowOfDevice(@RequestBody DeviceMaxFlow deviceMaxFlow){
        StatisticsData.addDeviceMaxFlowConfig(deviceMaxFlow);
        return BaseResponse.OK();
    }

    @ApiOperation("DOS攻击配置")
    @PostMapping("dos_config")
    public BaseResponse dosConfig(@RequestBody DosConfig dosConfig) {
        AttackConfig.setCoSiteTimeGap(dosConfig.getCoSiteTime());
        AttackConfig.setCoSiteNum(dosConfig.getCoSiteNum());
        AttackConfig.setMultiSiteNum(dosConfig.getMulSiteNum());
        AttackConfig.setMultiSiteTimeGap(dosConfig.getMulSiteTime());
        return BaseResponse.OK();
    }

    @ApiOperation("攻击报文已处理")
    @PostMapping("handle_attacks")
    @Log
    public BaseResponse handleAttacks(List<String> attackPacketTimeStamp){
        int i = packetInfoMapper.handleAttackPacket(attackPacketTimeStamp);
        if (i != attackPacketTimeStamp.size()){
            //发送的报文数量和处理的报文数量相等
            return BaseResponse.ERROR(Common.HTTP_STATUS_CODE.ATTACK_HANDLE_ERROR,"攻击报文处理异常");
        }
        return BaseResponse.OK();
    }

    @ApiOperation("查询攻击")
    @PostMapping("get_attacks")
    @Log
    public BaseResponse getAttack(@RequestBody AttackF attackF){
        return BaseResponse.OK(packetInfoMapper.selectAttackBybadTypeAndLevel(attackF));
    }

    @ApiOperation("添加工艺参数攻击监测配置")
    @PostMapping("art_attack_config")
    public BaseResponse configArtAttack(@RequestBody ArtAttackConfig artAttackConfig){
        List<ArtAttack2Config> configs = artAttackConfig.getRule();
        List<String> list = new ArrayList<>();
        for (ArtAttack2Config config : configs) {
            list.add(config.getValue());
        }
        int id = ((Integer) packetInfoMapper.saveOrUpdateArtAttackConfig(artAttackConfig).data);
        AttackCommon.addArtAttackAnalyzeConfig(new ArtAttackAnalyzeConfig(list,
                artAttackConfig.getDetail(),artAttackConfig.isEnable(),id));
        return BaseResponse.OK();
    }

    @ApiOperation("修改攻击配置状态")
    @PostMapping("change_attack_config_state")
    public BaseResponse changeAttackConfigState(@RequestBody AttackConfigState attackConfigState){
        packetInfoMapper.updateArtAttackConfigState(attackConfigState.getId(),attackConfigState.isEnable());
        AttackCommon.changeArtAttackAnalyzeConfigState(attackConfigState.getId(),attackConfigState.isEnable());
        return BaseResponse.OK();
    }

    @ApiOperation("删除工艺参数攻击配置")
    @DeleteMapping("delete_art_attack_config")
    public BaseResponse deleteArtAttackConfig(@RequestParam int id){
        packetInfoMapper.deleteArtAttackConfig(id);
        AttackCommon.removeArtAttackAnalyzeConfig(new ArtAttackAnalyzeConfig(id));
        return BaseResponse.OK();
    }

    @ApiOperation("获取工艺参数攻击配置")
    @PostMapping("paged_art_attack_config")
    public BaseResponse getPagedArtAttackConfig(@RequestBody PagedArtAttackConfig pagedArtAttackConfig){
        return BaseResponse.OK(packetInfoMapper.selectArtAttackConfigPaged(pagedArtAttackConfig.getLimit(),
                pagedArtAttackConfig.getPage()));
    }

}
