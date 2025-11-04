package postgresImplementationDao;

import dao.HackathonDAO;
import database.DatabaseConnection;
import java.sql.*;
import java.util.List;

/**
 * Implementazione dell'interfaccia {@link HackathonDAO} per la gestione dei dati
 * relativi agli hackathon nel database PostgreSQL.
 */
public class HackathonImplementationDAO implements HackathonDAO {
    private Connection connection;

    /**
     * Costruisce un nuovo oggetto {@code HackathonImplementationDAO} e
     * inizializza la connessione al database.
     */
    public HackathonImplementationDAO() {
        try {
            connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void overallRanking(List<String> teamNames, List<Double> scores, List<String> titles, List<String> locations) throws SQLException {
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
        rs.close();
    }

    @Override
    public void scoreboard(String title, String location, List<String> teamNames, List<Double> scores) throws SQLException {
        connection.setAutoCommit(false);
        CallableStatement cs;
        String query = "{ ? = CALL scoreboard((SELECT id_hackathon " +
                                              "FROM Hackathon " +
                                              "WHERE titolo = ? AND sede = ?)) }";
        cs = connection.prepareCall(query);
        cs.registerOutParameter(1, Types.OTHER);
        cs.setString(2, title);
        cs.setString(3, location);
        cs.execute();
        ResultSet rs = (ResultSet) cs.getObject(1);

        while (rs.next()) {
            teamNames.add(rs.getString("nome_team"));
            scores.add(rs.getDouble("voto_finale"));
        }
        rs.close();
        connection.commit();
        connection.setAutoCommit(true);
    }

    @Override
    public void getClosedHackathons(List<String> titles, List<String> locations) throws SQLException {
        PreparedStatement ps;
        String query = "SELECT titolo, sede " +
                        "FROM Hackathon " +
                        "WHERE data_fine <= CURRENT_DATE;";
        ps = connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            titles.add(rs.getString("titolo"));
            locations.add(rs.getString("sede"));
        }
        rs.close();
    }
}