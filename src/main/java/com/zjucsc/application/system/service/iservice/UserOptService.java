package com.zjucsc.application.system.service.iservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjucsc.application.system.entity.User;
import com.zjucsc.base.BaseResponse;

import java.util.List;

public interface UserOptService extends IService<User> {
    List<User> getAllUsers();

    boolean logout(String userName);

    void login(String userName);

    List<String> getAllLogginedUsers();

    void saveToken(User user , String token);

    String getTokenRole(String token);
}
