package com.zjucsc.application.system.service.hessian_iservice;

import com.zjucsc.application.domain.bean.AttackInfo;
import com.zjucsc.application.system.mapper.base.IService;

import java.util.List;

/**
 * @author hongqianhui
 */
public interface IAttackInfoService extends IService<AttackInfo> {
    void saveAttackInfo(List<AttackInfo> infoList);

    List<AttackInfo> selectAll();
}
