package com.zjucsc.application.system.service.hessian_iservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjucsc.application.system.entity.AttackInfo;

import java.util.List;

/**
 * @author hongqianhui
 */
public interface IAttackInfoService  {
    void saveAttackInfo(List<AttackInfo> infoList);
}
