package com.zjucsc.application.system.service.hessian_mapper;

import com.zjucsc.application.domain.bean.AttackInfo;
import com.zjucsc.application.system.mapper.base.BaseMapper;

import java.util.List;

/**
 * @author hongqianhui
 */
public interface AttackInfoMapper extends BaseMapper<AttackInfo> {
    public void saveAttackInfo(List<AttackInfo> infoList);

    public List<AttackInfo> selectAll();
}
