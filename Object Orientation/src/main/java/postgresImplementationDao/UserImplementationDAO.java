package postgresImplementationDao;

import dao.UserDAO;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserImplementationDAO implements UserDAO {
    private Connection connection;

    public UserImplementationDAO() {
        try {
            connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void newUser(String username, String name, String surname, String password)  throws SQLException{
        PreparedStatement query;
        query = connection.prepareCall("CALL new_user(?, ?, ?, ?)");
        query.setString(1, username);
        query.setString(2, name);
        query.setString(3, surname);
        query.setString(4, password);
        query.executeUpdate();
    }
}
