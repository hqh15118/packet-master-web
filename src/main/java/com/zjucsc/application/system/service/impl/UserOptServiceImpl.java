package com.zjucsc.application.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.system.dao.UserOptMapper;
import com.zjucsc.application.domain.entity.User;
import com.zjucsc.application.system.service.iservice.UserOptService;
import com.zjucsc.base.BaseResponse;
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

    @Override
    public BaseResponse logout(String userName) {
        if (Common.LOGGINED_USERS.contains(userName)){
            Common.LOGGINED_USERS.remove(userName);
            return BaseResponse.OK();
        }
        else{
            return BaseResponse.ERROR(Common.HTTP_STATUS_CODE.NOT_FOUND,"用户不存在或未登录");
        }
    }

    @Override
    public void login(String userName) {
        Common.LOGGINED_USERS.add(userName);
    }

    @Transactional
    @Override
    public boolean onServer(String userName) {
        return Common.LOGGINED_USERS.contains(userName);
    }

    @Transactional
    @Override
    public List<String> getAllLogginedUsers() {
        return Common.LOGGINED_USERS;
    }

    @Override
    public void saveToken(User user , String token) {
        user.setToken(token);
        updateById(user);
    }

    @Override
    public String getTokenRole(String token) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("token" , token);
        User user1 = getOne(userQueryWrapper);
        if (user1!=null)
            return user1.getRole();
        else
            return null;
    }
}
