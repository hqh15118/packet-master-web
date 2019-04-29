package com.zjucsc.application.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjucsc.application.domain.entity.User;

import java.util.List;

public interface UserOptMapper extends BaseMapper<User> {

    List<User> getAllUserInfo();

}
