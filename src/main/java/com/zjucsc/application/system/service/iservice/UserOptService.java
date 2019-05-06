package com.zjucsc.application.system.service.iservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjucsc.application.domain.entity.User;
import com.zjucsc.base.BaseResponse;

import java.util.List;

public interface UserOptService extends IService<User> {
    List<User> getAllUsers();

    BaseResponse logout(String userName);

    void login(String userName);

    boolean onServer(String userName);

    List<String> getAllLogginedUsers();

    void saveToken(User user , String token);
}
