package com.zjucsc.application.controller;


import com.alibaba.fastjson.JSON;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.config.PACKET_PROTOCOL;
import com.zjucsc.application.config.StatisticsData;
import com.zjucsc.application.config.auth.Log;
import com.zjucsc.application.domain.bean.*;
import com.zjucsc.application.domain.non_hessian.DeviceMaxFlow;
import com.zjucsc.application.domain.bean.RightPacketInfo;
import com.zjucsc.application.system.service.hessian_mapper.DeviceMaxFlowMapper;
import com.zjucsc.application.system.service.hessian_mapper.OptAttackMapper;
import com.zjucsc.application.system.service.hessian_mapper.PacketInfoMapper;
import com.zjucsc.application.util.CommonCacheUtil;
import com.zjucsc.application.util.ConfigUtil;
import com.zjucsc.attack.base.BaseOptConfig;
import com.zjucsc.attack.bean.ArtAttackAnalyzeConfig;
import com.zjucsc.attack.bean.AttackConfig;
import com.zjucsc.attack.AttackCommon;
import com.zjucsc.attack.modbus.ModbusOptConfig;
import com.zjucsc.attack.pn_io.PnioOptConfig;
import com.zjucsc.attack.s7comm.S7OptAttackConfig;
import com.zjucsc.common.exceptions.ProtocolIdNotValidException;
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
        StatisticsData.addDeviceMaxFlowConfig(deviceMaxFlow);
        return BaseResponse.OK();
    }

    @ApiOperation("DOS攻击配置")
    @PostMapping("new_dos_config")
    public BaseResponse dosConfig(@RequestBody DosConfig dosConfig) {
        AttackConfig.setCoSiteTimeGap(dosConfig.getCoSiteTime());
        AttackConfig.setCoSiteNum(dosConfig.getCoSiteNum());
        AttackConfig.setMultiSiteNum(dosConfig.getMulSiteNum());
        AttackConfig.setMultiSiteTimeGap(dosConfig.getMulSiteTime());
        ConfigUtil.setData("cosite_timegap",String.valueOf(dosConfig.getCoSiteTime()));
        ConfigUtil.setData("cosite_num",String.valueOf(dosConfig.getCoSiteNum()));
        ConfigUtil.setData("multisite_timegap",String.valueOf(dosConfig.getMulSiteTime()));
        ConfigUtil.setData("multisite_num",String.valueOf(dosConfig.getMulSiteNum()));
        return BaseResponse.OK();
    }

    @ApiOperation("获取Dos攻击配置")
    @GetMapping("get_dos_config")
    public BaseResponse getDosConfigs(){
        DosConfig dosConfig = new DosConfig();
        dosConfig.setCoSiteNum(AttackConfig.getCoSiteNum());
        dosConfig.setCoSiteTime(AttackConfig.getCoSiteTimeGap());
        dosConfig.setMulSiteTime(AttackConfig.getMultiSiteTimeGap());
        dosConfig.setMulSiteNum(AttackConfig.getMultiSiteNum());
        return BaseResponse.OK(dosConfig);
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
        if (protocolId == PACKET_PROTOCOL.S7_ID){
            S7OptAttackConfig s7OptAttackConfig = JSON.parseObject(jsonData,S7OptAttackConfig.class);
            AttackCommon.addOptAttackConfig(s7OptAttackConfig);
        }else if (protocolId == PACKET_PROTOCOL.MODBUS_ID){
            ModbusOptConfig modbusOptConfig = JSON.parseObject(jsonData,ModbusOptConfig.class);
            AttackCommon.addOptAttackConfig(modbusOptConfig);
        }else if (protocolId == PACKET_PROTOCOL.PN_IO_ID){
            PnioOptConfig pnioOptConfig = JSON.parseObject(jsonData,PnioOptConfig.class);
            AttackCommon.addOptAttackConfig(pnioOptConfig);
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
        List configs = optAttackMapper.selectAllOptAttackConfigByProtocol(optAttackPage.getProtocolId() , optAttackPage.getPage() ,
                optAttackPage.getLimit());
        return BaseResponse.OK(configs);
    }
}
