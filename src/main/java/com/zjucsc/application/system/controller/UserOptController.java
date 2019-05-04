package com.zjucsc.application.system.controller;

import com.zjucsc.application.config.Common;
import com.zjucsc.base.BaseResponse;
import com.zjucsc.application.domain.entity.User;
import com.zjucsc.application.system.service.UserOptService;
import com.zjucsc.base.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user/")
public class UserOptController {

    @Autowired
    private UserOptService userOptService;

    @PostMapping("login")
    public BaseResponse login(@RequestBody @Valid User user){
        User loginUser = userOptService.getById(user.getName());
        if (loginUser == null){
            return BaseResponse.ERROR(404,"用户不存在");
        }else{
            if (loginUser.getPassword().equals(MD5Util.encrypt(user.getPassword()))){
                userOptService.login(user.getName());
                return BaseResponse.OK(Common.SOCKET_IO_PORT);
            }else{
                return BaseResponse.ERROR(401,"密码错误");
            }
        }
    }

    @PostMapping("register")
    public BaseResponse register(@RequestBody @Valid User user){
        User loginUser = userOptService.getById(user.getName());
        if (loginUser!=null){
            return BaseResponse.ERROR(400,"用户名已存在");
        }else{
            user.setPassword(MD5Util.encrypt(user.getPassword()));
            userOptService.save(user);
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
    public BaseResponse addUser(@RequestBody @Valid User user){
        return register(user);
    }

    //TODO quanxian
    @GetMapping("all_loginned_user")
    public BaseResponse getAllLogginedUser(){
        return BaseResponse.OK(userOptService.getAllLogginedUsers());
    }
}
