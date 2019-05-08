package com.zjucsc.application.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjucsc.application.system.entity.AttackInfo;
import com.zjucsc.application.system.mapper.filter.AttackInfoMapper;
import com.zjucsc.application.system.service.iservice.IAttackInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hongqianhui
 */
@Service
public class AttackInfoServiceImpl extends ServiceImpl<AttackInfoMapper, AttackInfo> implements IAttackInfoService {

    @Override
    public void saveAttackInfo(List<AttackInfo> infoList) {
        saveBatch(infoList);
    }
}
