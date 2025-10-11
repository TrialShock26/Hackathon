package postgresImplementationDao;

import dao.TeamDAO;
import database.DatabaseConnection;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class TeamImplementationDAO implements TeamDAO {
    private Connection connection;

    public TeamImplementationDAO() {
        try {
            connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void publishProgress(String teamName, String hackTitle, String location, String docTitle, String content) throws SQLException {
        CallableStatement cs;
        String query = "DO $$ " +
                "DECLARE " +
                "  teamId Team.id_team%TYPE; " +
                "BEGIN " +
                "    SELECT id_team INTO teamId " +
                "    FROM Team NATURAL JOIN Hackathon " +
                "    WHERE nome = ? AND titolo = ? AND sede = ?; " +
                "    CALL publish_progress(teamId, ?, ?); " +
                "END " +
                "$$;";
        cs = connection.prepareCall(query);
        cs.setString(1, teamName);
        cs.setString(2, hackTitle);
        cs.setString(3, location);
        cs.setString(4, content);
        cs.setString(5, docTitle);
        cs.execute();
    }
}