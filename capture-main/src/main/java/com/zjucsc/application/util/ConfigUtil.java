package com.zjucsc.application.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.sql.*;
import java.util.Properties;

@Slf4j
public class ConfigUtil {
    private static Connection connection;
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:local.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (connection!=null) {
            try (Statement statement = connection.createStatement()) {
                statement.execute("CREATE TABLE IF NOT EXISTS key_value(\n" +
                        "  k VARCHAR(64) PRIMARY KEY ,\n" +
                        "  v VARCHAR(64)\n" +
                        ")");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static PreparedStatement preparedStatement;
    private static PreparedStatement preparedStatement1;
    private static PreparedStatement preparedStatement2;
    public synchronized static String getData(String key,String def){
        if (preparedStatement == null){
            try {
                preparedStatement  = connection.prepareStatement("SELECT k,v FROM key_value WHERE key_value.k = ?");
            } catch (SQLException e) {
                return def;
            }
        }
            try {
                preparedStatement.setString(1,key);
                preparedStatement.execute();
                ResultSet resultSet = preparedStatement.getResultSet();
                String res =  resultSet.getString("v");
                return res == null ? def : res;
            } catch (SQLException e) {
                return def;
            }
    }

    public synchronized static boolean setData(String k,String v){
        if (preparedStatement1 == null){
            try {
                preparedStatement1 = connection.prepareStatement("INSERT INTO key_value VALUES (?,?)");
                preparedStatement2 = connection.prepareStatement("DELETE FROM key_value WHERE k = ? ");
            } catch (SQLException ignored) { }
        }
        if (preparedStatement1!=null){
            try {
                preparedStatement2.setString(1,k);
                preparedStatement2.execute();
                preparedStatement1.setString(1,k);
                preparedStatement1.setString(2,v);
                preparedStatement1.execute();
            } catch (SQLException e) {
                return false;
            }
            return true;
        }
        return false;
    }

}
