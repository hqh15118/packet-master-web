package deploy.repository;

import java.sql.SQLException;

public interface ILoginRepository {
    boolean login(String user,String password) throws SQLException;
}
