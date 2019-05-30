package com.zjucsc.application.system.service.hessian_mapper;

import com.zjucsc.application.system.entity.User;
import com.zjucsc.application.system.mapper.base.BaseMapper;

import java.util.List;

public interface UserOptMapper extends BaseMapper<User> {

    List<User> getAllUserInfo();

    User getRoleByToken(String token);
}
