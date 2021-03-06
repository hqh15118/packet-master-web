package com.zjucsc.application.system.service.hessian_impl;

import com.zjucsc.application.config.Common;
import com.zjucsc.application.domain.bean.User;
import com.zjucsc.application.system.mapper.base.BaseServiceImpl;
import com.zjucsc.application.system.service.hessian_iservice.UserOptService;
import com.zjucsc.application.system.service.hessian_mapper.UserOptMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserOptServiceImpl extends BaseServiceImpl< User,UserOptMapper> implements UserOptService {

    @Override
    public List<User> getAllUsers() {
        return baseMapper.getAllUserInfo();
    }

    @Override
    public boolean logout(String userName) {
        if (Common.LOGGINED_USERS.contains(userName)){
            Common.LOGGINED_USERS.remove(userName);
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public void login(String userName) {
        Common.LOGGINED_USERS.add(userName);
    }

    @Override
    public List<String> getAllLogginedUsers() {
        return Common.LOGGINED_USERS;
    }

    @Override
    public void saveToken(User user , String token) {
        user.setToken(token);
        this.baseMapper.updateById(user);
    }

    @Override
    public String getTokenRole(String token) {
        User user1 = this.baseMapper.getRoleByToken(token);
        if (user1!=null)
            return user1.getRole();
        else
            return null;
    }
}
