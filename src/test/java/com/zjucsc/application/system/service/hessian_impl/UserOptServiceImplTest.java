package com.zjucsc.application.system.service.hessian_impl;

import com.zjucsc.application.system.service.hessian_mapper.UserOptMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserOptServiceImplTest {

    @Autowired private UserOptMapper userOptMapper;

    @Test
    public void getAllUsers() {
        System.out.println(userOptMapper.getAllUserInfo());
    }

    @Test
    public void logout() {

    }

    @Test
    public void login() {

    }

    @Test
    public void getAllLogginedUsers() {
    }

    @Test
    public void saveToken() {

    }

    @Test
    public void getTokenRole() {
        System.out.println(userOptMapper.getRoleByToken(""));
    }
}