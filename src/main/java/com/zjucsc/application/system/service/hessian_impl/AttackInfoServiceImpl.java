package com.zjucsc.application.system.service.hessian_impl;


import com.zjucsc.application.domain.bean.AttackInfo;
import com.zjucsc.application.system.mapper.base.BaseServiceImpl;
import com.zjucsc.application.system.service.hessian_iservice.IAttackInfoService;
import com.zjucsc.application.system.service.hessian_mapper.AttackInfoMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttackInfoServiceImpl extends BaseServiceImpl<AttackInfoMapper, AttackInfo> implements IAttackInfoService {
    @Override
    public void saveAttackInfo(List<AttackInfo> infoList) {
        this.baseMapper.saveAttackInfo(infoList);
    }

    @Override
    public List<AttackInfo> selectAll() {
        return this.baseMapper.selectAll();
    }
}
