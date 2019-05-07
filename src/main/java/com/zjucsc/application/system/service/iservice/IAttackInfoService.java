package com.zjucsc.application.system.service.iservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjucsc.application.domain.entity.AttackInfo;

import java.util.List;

/**
 * @author hongqianhui
 */
public interface IAttackInfoService extends IService<AttackInfo> {
    void saveAttackInfo(List<AttackInfo> infoList);
}
