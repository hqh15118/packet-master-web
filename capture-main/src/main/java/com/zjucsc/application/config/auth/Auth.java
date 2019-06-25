package com.zjucsc.application.config.auth;

public interface Auth {
    String ADMIN = "admin";
    String VISITOR = "visitor";
    String OPERATOR = "operator";

    int ADMIN_ID = Integer.MAX_VALUE;
    int VISITOR_ID = 0;
    int OPERATOR_ID = 100;
}
