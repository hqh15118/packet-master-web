package com.zjucsc.application.controller;


import com.alibaba.fastjson.JSON;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.config.PACKET_PROTOCOL;
import com.zjucsc.application.config.auth.Log;
import com.zjucsc.application.domain.bean.*;
import com.zjucsc.application.domain.non_hessian.DeviceMaxFlow;
import com.zjucsc.application.domain.bean.RightPacketInfo;
import com.zjucsc.application.system.service.hessian_mapper.DeviceMaxFlowMapper;
import com.zjucsc.application.system.service.hessian_mapper.DosConfigMapper;
import com.zjucsc.application.system.service.hessian_mapper.OptAttackMapper;
import com.zjucsc.application.system.service.hessian_mapper.PacketInfoMapper;
import com.zjucsc.application.util.CommonCacheUtil;
import com.zjucsc.application.util.ConfigUtil;
import com.zjucsc.attack.bean.*;
import com.zjucsc.attack.AttackCommon;
import com.zjucsc.attack.config.ModbusOptConfig;
import com.zjucsc.attack.config.PnioOptConfig;
import com.zjucsc.attack.config.S7OptAttackConfig;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hongqianhui
 */
@RestController
@RequestMapping("/attack")
public class AttackConfigController {

    @Autowired private PacketInfoMapper packetInfoMapper;
    @Autowired private OptAttackMapper optAttackMapper;
    @Autowired private DeviceMaxFlowMapper deviceMaxFlowMapper;
    @Autowired private DosConfigMapper dosConfigMapper;

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
        ConfigUtil.setData("max_flow",String.valueOf(flowInByte));
        return BaseResponse.OK();
    }

    @GetMapping("get_max_flow")
    @ApiOperation("获取配置过的一定时间内的总最大流量")
    public BaseResponse getConfigurationMaxFlow(){
        return BaseResponse.OK(Common.maxFlowInByte);
    }

    @ApiOperation("配置一定时间内的设备的最大流量，超过报警")
    @PostMapping("device_max_flow")
    @Log
    public BaseResponse configMaxFlowOfDevice(@RequestBody DeviceMaxFlow deviceMaxFlow){
        deviceMaxFlowMapper.insertByDeviceNumber(deviceMaxFlow);
        CommonCacheUtil.addOrUpdateDeviceMaxFlow(deviceMaxFlow);
        return BaseResponse.OK();
    }

    @ApiOperation("DOS攻击配置")
    @PostMapping("new_dos_config")
    public BaseResponse dosConfig(@RequestBody DosConfig dosConfig) {
        dosConfigMapper.addOrUpdateDosConfig(dosConfig);
        return BaseResponse.OK(AttackCommon.addOrUpdateDosAnalyzePoolEntry(CommonCacheUtil.getTargetDeviceTagByNumber(dosConfig.getDeviceNumber()),dosConfig));
    }

    @ApiOperation("获取Dos攻击配置")
    @GetMapping("get_dos_config")
    public BaseResponse getDosConfigs(@RequestParam String deviceNumber){
        return BaseResponse.OK(dosConfigMapper.selectDosConfigByDeviceNumber(deviceNumber));
    }

    @ApiOperation("删除Dos攻击配置")
    @PostMapping("remove_dos_config")
    public BaseResponse removeDosConfig(@RequestBody DosConfig dosConfig){
        dosConfigMapper.removeDosConfig(dosConfig);
        String deviceTag = CommonCacheUtil.getTargetDeviceTagByNumber(dosConfig.getDeviceNumber());
        AttackCommon.removeDosAnalyzePoolEntry(deviceTag,dosConfig.getProtocol());
        return BaseResponse.OK();
    }

    @ApiOperation("使能某个DOS配置")
    @PostMapping("enable_dos_config")
    public BaseResponse enableDosConfig(@RequestBody EnableDos enableDos){
        int id = enableDos.getId();
        boolean enable = enableDos.isEnable();
        DosConfig dosConfig = dosConfigMapper.changeDosConfigState(id,enable);
        String deviceTag = CommonCacheUtil.getTargetDeviceTagByNumber(dosConfig.getDeviceNumber());
        AttackCommon.changeDosConfig(deviceTag,dosConfig.getProtocol(),enable);
        return BaseResponse.OK();
    }

    @ApiOperation("获取设备配置的最大流量")
    @GetMapping("get_device_max_flow")
    public BaseResponse deviceMaxFlow(@RequestParam String deviceNumber){
        return BaseResponse.OK(deviceMaxFlowMapper.selectByDeviceNumber(deviceNumber));
    }

    @ApiOperation("攻击报文已处理")
    @PostMapping("handle_attacks")
    @Log
    public BaseResponse handleAttacks(@RequestBody List<String> attackPacketTimeStamp){
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

    @ApiOperation("添加/修改工艺参数攻击监测配置")
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

    @ApiOperation("设置正常报文五元组")
    @PostMapping("set_right_packet")
    public BaseResponse setRightPacket(@RequestBody List<RightPacketInfo> rightPacketInfo){
        packetInfoMapper.addNormalPacket(rightPacketInfo,Common.GPLOT_ID);
        for (RightPacketInfo packetInfo : rightPacketInfo) {
            CommonCacheUtil.addNormalRightPacketInfo(packetInfo);
        }
        return BaseResponse.OK();
    }

    @ApiOperation("添加协议操作攻击配置，id > 0表示更新；不填表示添加新的配置，顺序返回记录的ID列表")
    @PostMapping("new_opt_config")
    public BaseResponse addOrUpdateOptAttackConfig(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader bfr = request.getReader();
        String str;
        while ((str = bfr.readLine())!=null){
            sb.append(str);
        }
        String jsonData = sb.toString();
        BaseOptConfig baseConfig = JSON.parseObject(jsonData,BaseOptConfig.class);
        int protocolId = baseConfig.getProtocolId();
        optAttackMapper.addOrUpdateAllOptAttackConfigByProtocolAndId(baseConfig.getProtocolId(),baseConfig.getId(),jsonData);
        BaseOptConfig resConfig = null;
        if (protocolId == PACKET_PROTOCOL.S7_ID){
            resConfig = JSON.parseObject(jsonData,S7OptAttackConfig.class);
        }else if (protocolId == PACKET_PROTOCOL.MODBUS_ID){
            resConfig = JSON.parseObject(jsonData,ModbusOptConfig.class);
        }else if (protocolId == PACKET_PROTOCOL.PN_IO_ID){
            resConfig = JSON.parseObject(jsonData,PnioOptConfig.class);
        }
        if (resConfig!=null) {
            List<String> expression = new ArrayList<>();
            for (ArtOptWrapper artOptWrapper : baseConfig.getRule()) {
                expression.add(artOptWrapper.getValue());
            }
            resConfig.setExpression(expression);
            AttackCommon.addOptAttackConfig(resConfig);
        }
        return BaseResponse.OK();
    }

    @ApiOperation("删除协议操作攻击配置")
    @PostMapping("delete_opt_config")
    public BaseResponse deleteOptAttackConfig(@RequestBody OptAttackF attackF) {
        BaseOptConfig baseOptConfig = optAttackMapper.deleteOptAttackConfigById(attackF.getProtocolId(),attackF.getId());
        AttackCommon.deleteOptAttackConfig(baseOptConfig);
        return BaseResponse.OK();
    }

    @Log
    @ApiOperation("查询协议操作攻击配置")
    @PostMapping("opt_configs")
    public BaseResponse getAllOptAttackConfigsByProtocol(@RequestBody OptAttackPage optAttackPage) {
        List<BaseOptConfig> configs = optAttackMapper.selectAllOptAttackConfigByProtocol(optAttackPage.getProtocolId() , optAttackPage.getPage() ,
                optAttackPage.getLimit());
        return BaseResponse.OK(configs);
    }
}
