package com.zjucsc.application.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjucsc.application.system.dao.UserOptMapper;
import com.zjucsc.application.domain.entity.User;
import com.zjucsc.application.system.service.UserOptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service("userManagementService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserOptServiceImpl extends ServiceImpl<UserOptMapper, User> implements UserOptService {
    @Override
    public List<User> getAllUsers() {
        return baseMapper.getAllUserInfo();
    }
}
