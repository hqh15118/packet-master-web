package com.zjucsc.application.system.service.hessian_iservice;


import com.zjucsc.application.domain.bean.User;
import com.zjucsc.application.system.mapper.base.IService;

import java.util.List;

public interface UserOptService  extends IService<User> {
    List<User> getAllUsers();

    boolean logout(String userName);

    void login(String userName);

    List<String> getAllLogginedUsers();

    void saveToken(User user, String token);

    String getTokenRole(String token);
}
