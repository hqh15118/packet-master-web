package com.zjucsc.application.controller;


import com.zjucsc.application.config.Common;
import com.zjucsc.application.config.auth.Log;
import com.zjucsc.application.domain.bean.CositeDosConfigBean;
import com.zjucsc.application.system.service.hessian_iservice.IAttackInfoService;
import com.zjucsc.attack.bean.AttackConfig;
import com.zjucsc.base.BaseResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author hongqianhui
 */
@RestController
@RequestMapping("/attack")
public class AttackInfoController {

    @Autowired private IAttackInfoService iAttackInfoService;

    @RequestMapping(value = "/get_all_attack_info" , method = RequestMethod.GET)
    public BaseResponse getAttackInfo(){
        return BaseResponse.OK(iAttackInfoService.selectAll());
    }

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


}
