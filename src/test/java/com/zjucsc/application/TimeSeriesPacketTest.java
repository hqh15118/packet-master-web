package com.zjucsc.application;

import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.Date;
import java.util.Random;

public class TimeSeriesPacketTest {

    @Before
    public void initDriver() throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/packet_master_web" +
                "?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","hongqianhui1218");
    }

    @Test
    public void insertData() throws SQLException {
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO time_series_test(device_number, packet_number) VALUES(?,?) ");
            Random random = new Random();
            for (int i = 0; i < 100; i++) {
                preparedStatement.setString(1, "bc4fa4c1");
                preparedStatement.setInt(2, random.nextInt() * 100);
                preparedStatement.execute();
                Thread.sleep(5000);
            }
        }catch (InterruptedException | SQLException e) {
            e.printStackTrace();
        }finally {
            if (conn!=null){
                conn.close();
            }
        }
    }

    @Test
    public void createTable(){
        int gplotId = 10;
        Connection conn = null;
        try{
            conn = getConnection();
            Statement statement = conn.createStatement();
            String sqlBuilder = new StringBuilder().
                    append("CREATE TABLE IF NOT EXISTS packet_series_").
                    append(gplotId).
                    append("(\n").
                    append("  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,\n").
                    append("  `device_number` VARCHAR(64) NOT NULL ,\n").
                    append("  `time_stamp` DATETIME DEFAULT CURRENT_TIMESTAMP,\n").
                    append("  `packet_number` INT\n").
                    append(")ENGINE = INNODB;").
                    toString();
            statement.execute(sqlBuilder);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void selectData(){
        Connection conn = null;
        String querySQL = new StringBuilder().
                append("SELECT * FROM time_series_test WHERE DATE_SUB(CURRENT_TIMESTAMP,INTERVAL ").
                append("1 HOUR ) <= time_stamp AND device_number='bc4fa4c1';").
                toString();
        try{
            conn = getConnection();
            Statement statement = conn.createStatement();
            ResultSet set = statement.executeQuery(querySQL);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test(){
        System.out.println(new Date().toString());
    }
}
