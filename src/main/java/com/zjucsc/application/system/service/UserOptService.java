package com.zjucsc.application.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjucsc.application.domain.entity.User;

import java.util.List;

public interface UserOptService extends IService<User> {
    List<User> getAllUsers();
}
