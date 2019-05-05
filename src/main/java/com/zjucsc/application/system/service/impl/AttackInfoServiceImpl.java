package com.zjucsc.application.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjucsc.application.domain.entity.AttackInfo;
import com.zjucsc.application.system.dao.filter.AttackInfoMapper;
import com.zjucsc.application.system.service.iservice.IAttackInfoService;
import org.springframework.stereotype.Service;

/**
 * @author hongqianhui
 */
@Service
public class AttackInfoServiceImpl extends ServiceImpl<AttackInfoMapper, AttackInfo> implements IAttackInfoService {

}
