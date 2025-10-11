package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try {
            String url = "jdbc:postgresql://ep-restless-rain-a2a23td0-pooler.eu-central-1.aws.neon.tech/neondb?user=neondb_owner&password=npg_uRIqoh0V6lnb&sslmode=require&channelBinding=require";
            String user = "neondb_owner";
            String password = "npg_uRIqoh0V6lnb";
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }

    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null)
            instance = new DatabaseConnection();
        else if (instance.connection.isClosed())
            instance = new DatabaseConnection();
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return connection;
    }
}
