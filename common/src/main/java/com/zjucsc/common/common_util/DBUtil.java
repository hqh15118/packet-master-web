package com.zjucsc.common.common_util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    private static Connection connection;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static synchronized Connection getConnection(String JDBC_URL,String USER_NAME,String PASSWORD) throws SQLException {
        if (connection!=null){
            return connection;
        }
        connection = DriverManager.getConnection(JDBC_URL,USER_NAME,PASSWORD);
        return connection;
    }
}
