package com.zjucsc.application.controller;


import com.zjucsc.application.config.Common;
import com.zjucsc.application.config.auth.Log;
import com.zjucsc.application.domain.bean.CositeDosConfigBean;
import com.zjucsc.application.system.service.hessian_mapper.PacketInfoMapper;
import com.zjucsc.attack.bean.AttackConfig;
import com.zjucsc.base.BaseResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author hongqianhui
 */
@RestController
@RequestMapping("/attack")
public class AttackInfoController {

    @Autowired private PacketInfoMapper packetInfoMapper;
    /**
     * 配置一定时间内的最大流量，超过报警
     * @param flowInByte
     * @return
     */
    @ApiOperation("配置一定时间内的最大流量，超过报警")
    @GetMapping("max_flow")
    @Log
    public BaseResponse configMaxFlow(@RequestParam int flowInByte){
        Common.maxFlowInByte = flowInByte;
        return BaseResponse.OK();
    }

    @ApiOperation("配置同源DOS攻击")
    @PostMapping("codos_config")
    @Log
    public BaseResponse cositeDOSConfig(@RequestBody CositeDosConfigBean cositeDosConfigBean){
        AttackConfig.setCoSiteNum(cositeDosConfigBean.getMaxNum());
        AttackConfig.setCoSiteTimeGap(cositeDosConfigBean.getTimeInMill());
        return BaseResponse.OK();
    }

    @ApiOperation("配置多源DOS攻击")
    @PostMapping("muldos_config")
    @Log
    public BaseResponse mulsiteDOSConfig(@RequestBody CositeDosConfigBean mulsiteDosConfigBean){
        AttackConfig.setMultiSiteNum(mulsiteDosConfigBean.getMaxNum());
        AttackConfig.setCoSiteTimeGap(mulsiteDosConfigBean.getTimeInMill());
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

}
