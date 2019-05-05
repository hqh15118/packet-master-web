package com.zjucsc.application.system.controller;

import com.zjucsc.application.config.Common;
import com.zjucsc.base.BaseResponse;
import com.zjucsc.application.domain.entity.User;
import com.zjucsc.application.system.service.UserOptService;
import com.zjucsc.base.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("/user/")
public class UserOptController {

    @Autowired
    private UserOptService userOptService;

    @PostMapping("login")
    public BaseResponse login(@RequestBody @Valid User.UserForFront user){
        User loginUser = userOptService.getById(user.userName);
        if (loginUser == null){
            return BaseResponse.ERROR(404,"用户不存在");
        }else{
            if (loginUser.getPassword().equals(MD5Util.encrypt(user.password))){
                userOptService.login(user.userName);
                return BaseResponse.OK(Common.SOCKET_IO_PORT);
            }else{
                return BaseResponse.ERROR(401,"密码错误");
            }
        }
    }

    @PostMapping("register")
    public BaseResponse register(@RequestBody @Valid User.UserForFront user){
        User loginUser = userOptService.getById(user.password);
        if (loginUser!=null){
            return BaseResponse.ERROR(400,"用户名已存在");
        }else{
            User user1 = new User();
            user1.setName(user.userName);
            user1.setPassword(MD5Util.encrypt(user.password));
            user1.setDate(new Date().toString());
            user1.setRole(User.ROLE.ADMINISTRACOR);
            userOptService.save(user1);
            return BaseResponse.OK();
        }
    }
    //TODO quanxian
    @GetMapping("all_user")
    public BaseResponse getAllUser(){
        return BaseResponse.OK(userOptService.getAllUsers());
    }

    @GetMapping("logout")
    public BaseResponse logout(@RequestParam String userName){
        return userOptService.logout(userName);
    }

    @PostMapping("add_user")
    public BaseResponse addUser(@RequestBody @Valid User.UserForFront user){
        return register(user);
    }

    //TODO quanxian
    @GetMapping("all_loginned_user")
    public BaseResponse getAllLogginedUser(){
        return BaseResponse.OK(userOptService.getAllLogginedUsers());
    }
}
