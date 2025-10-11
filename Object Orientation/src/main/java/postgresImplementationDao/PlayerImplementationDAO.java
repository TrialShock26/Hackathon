package postgresImplementationDao;

import dao.PlayerDAO;
import database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;

public class PlayerImplementationDAO implements PlayerDAO {
    private Connection connection;

    public PlayerImplementationDAO() {
        try {
            connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void subscribe(String username, String title, String location) throws SQLException {
        CallableStatement cs;
        String query = "DO $$ " +
                "DECLARE " +
                "   hackId Hackathon.id_hackathon%TYPE; " +
                "BEGIN " +
                "   SELECT id_hackathon INTO hackId " +
                "   FROM Hackathon " +
                "   WHERE titolo = ? AND sede = ?; " +
                "   CALL subscribe(hackId, ?); " +
                "END " +
                "$$;";
        cs = connection.prepareCall(query);
        cs.setString(1, title);
        cs.setString(2, location);
        cs.setString(3, username);
        cs.execute();
    }

    @Override
    public void joinTeam(String username, String teamName, String title, String location) throws SQLException {
        CallableStatement cs;
        String query = "DO $$ " +
                "DECLARE " +
                "  teamId Team.id_team%TYPE; " +
                "BEGIN " +
                "    SELECT id_team INTO teamId " +
                "    FROM Team NATURAL JOIN Hackathon " +
                "    WHERE nome = ? AND titolo = ? AND sede = ?; " +
                "    CALL join_team(?, teamId); " +
                "END " +
                "$$;";
        cs = connection.prepareCall(query);
        cs.setString(1, teamName);
        cs.setString(2, title);
        cs.setString(3, location);
        cs.setString(4, username);
    }

    @Override
    public void getHackathons(String username, ArrayList<String> titles, ArrayList<String> locations, ArrayList<String> teamNames) throws SQLException {
        PreparedStatement ps;
        String query = "SELECT titolo, sede, nome " +
                       "FROM Hackathon NATURAL JOIN Team NATURAL JOIN Partecipazione " +
                       "WHERE id_partecipante = (SELECT id_partecipante " +
                                                "FROM Partecipante " +
                                                "WHERE username = ?);";
        ps = connection.prepareStatement(query);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            titles.add(rs.getString("titolo"));
            locations.add(rs.getString("sede"));
            teamNames.add(rs.getString("nome"));
        }
    }

    @Override
    public void getTeammates(String username, String teamName, String title, String location, ArrayList<String> names, ArrayList<String> surnames) throws SQLException {
        PreparedStatement ps;
        String query = "SELECT u.nome, cognome " +
                "FROM Partecipante p NATURAL JOIN Partecipazione NATURAL JOIN Team t NATURAL JOIN Hackathon JOIN Utente u ON u.username = p.username " +
                "WHERE t.nome = ? AND titolo = ? AND sede = ? AND u.username <> ?;";
        ps = connection.prepareStatement(query);
        ps.setString(1, teamName);
        ps.setString(2, title);
        ps.setString(3, location);
        ps.setString(4, username);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            names.add(rs.getString("nome"));
            surnames.add(rs.getString("cognome"));
        }
    }
}