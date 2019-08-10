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
import com.zjucsc.application.util.CacheUtil;
import com.zjucsc.application.util.CommonConfigUtil;
import com.zjucsc.application.util.ConfigUtil;
import com.zjucsc.attack.bean.*;
import com.zjucsc.attack.AttackCommon;
import com.zjucsc.attack.config.*;
import com.zjucsc.common.exceptions.ProtocolIdNotValidException;
import io.swagger.annotations.ApiOperation;
import kafka.utils.Json;
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
        CacheUtil.addOrUpdateDeviceMaxFlow(deviceMaxFlow);
        return BaseResponse.OK();
    }

    @ApiOperation("DOS攻击配置")
    @PostMapping("new_dos_config")
    public BaseResponse dosConfig(@RequestBody DosConfig dosConfig) {
        dosConfigMapper.addOrUpdateDosConfig(dosConfig);
        return BaseResponse.OK(AttackCommon.addOrUpdateDosAnalyzePoolEntry(CacheUtil.getTargetDeviceTagByNumber(dosConfig.getDeviceNumber()),dosConfig));
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
        String deviceTag = CacheUtil.getTargetDeviceTagByNumber(dosConfig.getDeviceNumber());
        AttackCommon.removeDosAnalyzePoolEntry(deviceTag,dosConfig.getProtocol());
        return BaseResponse.OK();
    }

    @ApiOperation("使能某个DOS配置")
    @PostMapping("enable_dos_config")
    public BaseResponse enableDosConfig(@RequestBody EnableDos enableDos){
        int id = enableDos.getId();
        boolean enable = enableDos.isEnable();
        DosConfig dosConfig = dosConfigMapper.changeDosConfigState(id,enable);
        String deviceTag = CacheUtil.getTargetDeviceTagByNumber(dosConfig.getDeviceNumber());
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
//        List<ArtAttack2Config> configs = artAttackConfig.getRule();
//        List<String> list = new ArrayList<>();
//        for (ArtAttack2Config config : configs) {
//            list.add(config.getValue());
//        }
//        int id = ((Integer) packetInfoMapper.saveOrUpdateArtAttackConfig(artAttackConfig).data);
//        AttackCommon.addArtAttackAnalyzeConfig(new ArtAttackAnalyzeConfig(list,
//                artAttackConfig.getDetail(),artAttackConfig.isEnable(),id));
        packetInfoMapper.saveOrUpdateArtAttackConfig(artAttackConfig);
        return BaseResponse.OK();
    }

    public static void addArtAttackConfig2Cache( List<ArtAttackConfigDB> configDBS) throws ProtocolIdNotValidException {
        for (ArtAttackConfigDB configDB : configDBS) {
            if (configDB.isEnable()){
                List<String> strings = new ArrayList<>();
                List<ArtAttack2Config> artAttack2Configs = JSON.parseArray(configDB.getRuleJson(),ArtAttack2Config.class);
                for (ArtAttack2Config artAttack2Config : artAttack2Configs)
                {
                    strings.add(artAttack2Config.getValue());
                }
                String protocol = CacheUtil.convertIdToName(configDB.getProtocolId());
                AttackCommon.addArtAttackAnalyzeConfig(protocol,new ArtAttackAnalyzeConfig(strings,configDB.getDetail(),
                        configDB.isEnable(),configDB.getId()));
            }
        }
    }

    @ApiOperation("修改攻击配置状态")
    @PostMapping("change_attack_config_state")
    public BaseResponse changeAttackConfigState(@RequestBody AttackConfigState attackConfigState) throws ProtocolIdNotValidException {
        packetInfoMapper.updateArtAttackConfigState(attackConfigState.getId(),attackConfigState.isEnable());
        List<ArtAttackConfigDB> configDBS = packetInfoMapper.selectArtAttackConfigPaged(999,1);
        AttackCommon.removeAllArtAttackAnalyzeConfig(attackConfigState.getProtocol());
        addArtAttackConfig2Cache(configDBS);
        return BaseResponse.OK();
    }

    @ApiOperation("删除工艺参数攻击配置")
    @DeleteMapping("delete_art_attack_config")
    public BaseResponse deleteArtAttackConfig(@RequestParam int id , @RequestParam String protocol){
        packetInfoMapper.deleteArtAttackConfig(id);
        AttackCommon.removeArtAttackAnalyzeConfig(protocol,new ArtAttackAnalyzeConfig(id));
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
            CacheUtil.addNormalRightPacketInfo(packetInfo);
        }
        return BaseResponse.OK();
    }

    @ApiOperation("删除正常报文五元组")
    @PostMapping("del_right_packet")
    public BaseResponse removeRightPacket(@RequestBody List<RightPacketInfo> rightPacketInfos){
        packetInfoMapper.removeNormalPacket(rightPacketInfos,Common.GPLOT_ID);
        for (RightPacketInfo packetInfo : rightPacketInfos) {
            CacheUtil.removeNormalRightPacketInfo(packetInfo);
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
        //直接存到数据库里，不启用
        optAttackMapper.addOrUpdateAllOptAttackConfigByProtocolAndId(baseConfig.getProtocolId(),baseConfig.getId(),jsonData);
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

    @ApiOperation("配置工艺参数操作攻击enable")
    @PostMapping("enable_opt_config")
    public BaseResponse enableOptConfig(@RequestBody OptConfigEnable optConfigEnable) {
        BaseOptConfig baseConfig = optAttackMapper.changeOptConfigStateByOpName(optConfigEnable.getProtocolId(),optConfigEnable.getOpName(),optConfigEnable.isEnable());
//        if (optConfigEnable.isEnable()) {
            AttackCommon.addOptAttackConfig(baseConfig);
            baseConfig.setEnable(true);
//        }else{
//            AttackCommon.removeArtOptAttackConfig(baseConfig);
//        }
        return BaseResponse.OK();
    }


    private BaseOptConfig getArtOptAttackConfig(String jsonData) throws ProtocolIdNotValidException {
        BaseOptConfig baseConfig = JSON.parseObject(jsonData,BaseOptConfig.class);
        BaseOptConfig resConfig;
        int protocolId = baseConfig.getProtocolId();
        switch (protocolId)
        {
            case PACKET_PROTOCOL.S7_ID : resConfig = JSON.parseObject(jsonData,S7OptAttackConfig.class);break;
            case PACKET_PROTOCOL.MODBUS_ID : resConfig = JSON.parseObject(jsonData,ModbusOptConfig.class);break;
            case PACKET_PROTOCOL.OPC_UA_ID : resConfig = JSON.parseObject(jsonData,OpcuaOptConfig.class);break;
            case PACKET_PROTOCOL.PN_IO_ID : resConfig = JSON.parseObject(jsonData,PnioOptConfig.class);break;
            case PACKET_PROTOCOL.IEC104_ASDU_ID : resConfig = JSON.parseObject(jsonData, IEC104Opconfig.class);break;
            case PACKET_PROTOCOL.DNP3_0_PRI_ID : resConfig = JSON.parseObject(jsonData,dnp3Opconfig.class);break;
            default:
                throw new ProtocolIdNotValidException("不支持添加协议ID为【" + protocolId + "】的操作攻击配置");
        }

        if (resConfig!=null) {
            List<String> expression = new ArrayList<>();
            for (ArtOptWrapper artOptWrapper : baseConfig.getRule()) {
                expression.add(artOptWrapper.getValue());
            }
            resConfig.setExpression(expression);
            AttackCommon.addOptAttackConfig(resConfig);
        }
        return resConfig;
    }
}
