package com.zjucsc.application.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjucsc.application.system.entity.User;

import java.util.List;

public interface UserOptMapper extends BaseMapper<User> {

    List<User> getAllUserInfo();

}
