package com.zjucsc.application.controller;

import com.zjucsc.application.config.Common;
import com.zjucsc.application.config.auth.Auth;
import com.zjucsc.application.config.auth.Log;
import com.zjucsc.application.config.auth.Token;
import com.zjucsc.application.domain.bean.BaseResponse;
import com.zjucsc.application.domain.bean.User;
import com.zjucsc.application.system.service.hessian_iservice.UserOptService;
import com.zjucsc.base.util.MD5Util;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/user/")
public class UserOptController {

    @Autowired
    private UserOptService userOptService;

    @Log
    @PostMapping("login")
    public BaseResponse login(@RequestBody @Valid User.UserForFront user){
        User loginUser = userOptService.getById(user.userName);
        if (loginUser == null){
            return BaseResponse.ERROR(404,"用户不存在");
        }else{
            if (loginUser.getPassword().equals(MD5Util.encrypt(user.password))){
                userOptService.login(user.userName);
                //HashMap<String,String[]> token = new HashMap<>();
                //token.put("roles",new String[]{"admin"});
                String token = MD5Util.encrypt(user.userName + user.password) + "_token";
                userOptService.saveToken(loginUser,token);
                return BaseResponse.OK(token);
            }else{
                return BaseResponse.ERROR(401,"密码错误");
            }
        }
    }

    //@Token(Auth.ADMIN_ID)
    @PostMapping("register")
    @Log
    public BaseResponse register(@RequestBody @Valid User.UserForFront user){
        User loginUser = userOptService.getById(user.password);
        if (loginUser!=null){
            return BaseResponse.ERROR(400,"用户名已存在");
        }else{
            User user1 = new User();
            user1.setName(user.userName);
            user1.setPassword(MD5Util.encrypt(user.password));
            user1.setDate(new Date().toString());
            user1.setRole(Auth.ADMIN);
            userOptService.insertById(user1);
            return BaseResponse.OK();
        }
    }


    @Log
    @Token(Auth.ADMIN_ID)
    @GetMapping("all_user")
    public BaseResponse getAllUser(){
        return BaseResponse.OK(userOptService.getAllUsers());
    }

    @Log
    @GetMapping("logout")
    public BaseResponse logout(@RequestParam String userName){
        if ( !userOptService.logout(userName)){
            return BaseResponse.ERROR(Common.HTTP_STATUS_CODE.NOT_FOUND,"用户不存在或未登录");
        }else{
            return BaseResponse.OK();
        }
    }

    @Log
    @Token(Auth.ADMIN_ID)
    @PostMapping("add_user")
    public BaseResponse addUser(@RequestBody @Valid User.UserForFront user){
        return register(user);
    }

    @Log
    @Token(Auth.ADMIN_ID)
    @GetMapping("all_on_user")
    public BaseResponse getAllLogginedUser(){
        return BaseResponse.OK(userOptService.getAllLogginedUsers());
    }

    @Log
    @GetMapping("get_user_info")
    public BaseResponse getUseInfo(@RequestParam String token){
        return BaseResponse.OK(new Wrapper(Collections.singletonList(userOptService.getTokenRole(token))));
    }

    @Data
    private static class Wrapper{
        private List<String> role;

        public Wrapper(List<String> role) {
            this.role = role;
        }
    }
}
