package postgresImplementationDao;

import dao.HackathonDAO;
import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;

public class HackathonImplementationDAO implements HackathonDAO {
    private Connection connection;

    public HackathonImplementationDAO() {
        try {
            connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void overallRanking(ArrayList<String> teamNames, ArrayList<Double> scores, ArrayList<String> titles, ArrayList<String> locations) throws SQLException {
        PreparedStatement ps;
        String query = "SELECT nome_team, voto_finale, titolo, sede " +
                       "FROM overall_ranking NATURAL JOIN Hackathon;";
        ps = connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            teamNames.add(rs.getString("nome_team"));
            scores.add(rs.getDouble("voto_finale"));
            titles.add(rs.getString("titolo"));
            locations.add(rs.getString("sede"));
        }
    }

    @Override
    public void scoreboard(String title, String location, ArrayList<String> teamNames, ArrayList<Double> scores) throws SQLException {
        CallableStatement cs;
        String query = "DO $$ " +
                "DECLARE " +
                "   hackId Hackathon.id_hackathon%TYPE; " +
                "BEGIN " +
                "   SELECT id_hackathon INTO hackId " +
                "   FROM Hackathon " +
                "   WHERE titolo = ? AND sede = ?; " +
                "   SELECT scoreboard(hackId) INTO ?; " +
                "END " +
                "$$;";
        cs = connection.prepareCall(query);
        cs.setString(1, title);
        cs.setString(2, location);
        cs.registerOutParameter(3, Types.OTHER);
        cs.execute();
        ResultSet rs = (ResultSet) cs.getObject(3);

        while (rs.next()) {
            teamNames.add(rs.getString("nome_team"));
            scores.add(rs.getDouble("voto_finale"));
        }
    }
}
