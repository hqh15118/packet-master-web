package com.zjucsc.application.controller.syscontroller;

import com.zjucsc.application.config.Common;
import com.zjucsc.application.config.auth.Auth;
import com.zjucsc.application.config.auth.Log;
import com.zjucsc.application.config.auth.Token;
import com.zjucsc.application.domain.bean.BaseResponse;
import com.zjucsc.application.domain.bean.User;
import com.zjucsc.application.domain.dto.UserDTO;
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
    public BaseResponse login(@RequestBody @Valid UserDTO userDTO){
        User loginUser = userOptService.getById(userDTO.getUserName());
        if (loginUser == null){
            return BaseResponse.ERROR(404,"用户不存在");
        }else{
            if (loginUser.getPassword().equals(MD5Util.encrypt(userDTO.getPassword()))){
                userOptService.login(userDTO.getUserName());
                String token = MD5Util.encrypt(userDTO.getUserName() + userDTO.getPassword()) + "_token";
                userOptService.saveToken(loginUser,token);
                return BaseResponse.OK(token);
            }else{
                return BaseResponse.ERROR(401,"密码错误");
            }
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
