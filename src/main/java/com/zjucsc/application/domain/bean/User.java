package com.zjucsc.application.domain.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class User  extends BaseResponse implements Serializable {
    private String name;
    private String password;
    private String role;
    private String date;
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
