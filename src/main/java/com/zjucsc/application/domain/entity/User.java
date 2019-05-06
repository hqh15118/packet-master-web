package com.zjucsc.application.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.sql.Date;

@Data
@TableName("user")
public class User {
    @TableId(value = "name")
    @NotNull
    private String name;
    @TableField(value = "password")
    @NotNull
    private String password;
    @TableField(value = "role")
    @NotNull
    private String role;
    @TableField(value = "last_login_time")
    private String date;
    @TableField("token")
    private String token;

    public static final class ROLE {
        public static final String ADMINISTRACOR = "ADMINISTRACTOR";
        public static final String OPERATOR = "OPERATOR";
        public static final String VISITOR = "VISITOR";
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", date=" + date +
                '}';
    }


    public static class UserForFront{
        public String userName;
        public String password;
    }
}
