package com.zjucsc.application.controller;


import com.zjucsc.application.config.Common;
import com.zjucsc.application.system.service.iservice.IAttackInfoService;
import com.zjucsc.base.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author hongqianhui
 */
@RestController
@RequestMapping("/attack")
public class AttackInfoController {

    @Autowired
    private IAttackInfoService service;

    @RequestMapping(value = "/get_all_attack_info" , method = RequestMethod.GET)
    public BaseResponse getAttackInfo(){
        return BaseResponse.OK(service.list());
    }

    /**
     * 配置一定时间内的最大流量，超过报警
     * @param flowInByte
     * @return
     */
    @GetMapping("max_flow")
    public BaseResponse configMaxFlow(@RequestParam int flowInByte){
        Common.maxFlowInByte = flowInByte;
        return BaseResponse.OK();
    }


}
