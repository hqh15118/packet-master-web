package deploy.repository;

import com.tonggong.mvc.AbstractRepository;
import deploy.controller.LoginController;
import deploy.util.DBUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginRepository  extends AbstractRepository<LoginController> implements ILoginRepository{
    public LoginRepository(LoginController controller) {
        super(controller);
    }

    @Override
    public boolean login(String user, String password) throws SQLException {
        String loginSQL = "SELECT password FROM user WHERE user.user_name=?";
        ResultSet resultSet = null;
        try (PreparedStatement preparedStatement = DBUtil.getConnection().prepareStatement(loginSQL)) {
            preparedStatement.setString(1, user);
            preparedStatement.execute();
            resultSet = preparedStatement.getResultSet();
            String resPassword = null;
            while (resultSet.next()) {
                resPassword = resultSet.getString("password");
            }
            return password.equals(resPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
}
