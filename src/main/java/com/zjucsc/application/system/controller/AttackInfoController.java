package com.zjucsc.application.system.controller;



import com.zjucsc.application.domain.entity.AttackInfo;
import com.zjucsc.application.system.service.iservice.IAttackInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author hongqianhui
 */
@RestController
@RequestMapping("/attack_info")
public class AttackInfoController {

    @Autowired
    private IAttackInfoService service;

    @RequestMapping(value = "/get_all_attack_info" , method = RequestMethod.GET)
    public List<AttackInfo> getAttackInfo(){
        return service.list();
    }


}
