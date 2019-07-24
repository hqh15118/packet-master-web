package deploy.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
    private static Connection connection;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:deploy.db");
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //
    public static synchronized Connection getConnection() throws SQLException {
        if (connection.isClosed()){
            connection = DriverManager.getConnection("jdbc:sqlite:deploy.db");
            connection.setAutoCommit(false);
            return connection;
        }else{
            return connection;
        }
    }

    public static void init() throws SQLException {
        try(Statement statement = connection.createStatement()) {
            String initSQL = "CREATE TABLE IF NOT EXISTS user(\n" +
                    "  user_name VARCHAR(64) PRIMARY KEY ,\n" +
                    "  password VARCHAR(64)\n" +
                    ")";
            statement.execute(initSQL);
        }
    }
}
