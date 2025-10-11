package postgresImplementationDao;

import dao.JudgeDAO;
import database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;

public class JudgeImplementationDAO implements JudgeDAO {
    private Connection connection;

    public JudgeImplementationDAO() {
        try {
            connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void publishProblem(String text, String title, String location) throws SQLException {
        CallableStatement cs;
        String query = "DO $$ " +
                "DECLARE " +
                "  hackId Hackathon.id_hackathon%TYPE; " +
                "BEGIN " +
                "    SELECT id_hackathon INTO hackId " +
                "    FROM Hackathon " +
                "    WHERE titolo = ? AND sede = ?; " +
                "    CALL publish_problem(hackId, ?); " +
                "END " +
                "$$;";
        cs = connection.prepareCall(query);
        cs.setString(1, title);
        cs.setString(2, location);
        cs.setString(3, text);
        cs.execute();
    }

    @Override
    public void examineDocument(String username, String docTitle, String content, String teamName, String hackTitle, String location) throws SQLException {
        PreparedStatement ps;
        String query = "DO $$ " +
                "DECLARE " +
                "   judgeId Giudice.id_giudice%TYPE; " +
                "   docId Documento.id_documento%TYPE; " +
                "BEGIN " +
                "   SELECT id_giudice INTO judgeId " +
                "   FROM Giudice " +
                "   WHERE username = ?; " +
                "   SELECT d.id_documento INTO docId " +
                "   FROM Documento d NATURAL JOIN Team t JOIN Hackathon h ON t.id_hackathon = h.id_hackathon " +
                "   WHERE d.titolo = ? AND d.contenuto = ? AND t.nome = ? AND h.titolo = ? AND h.sede = ?; " +
                "   INSERT INTO Esaminazione VALUES (judgeId, docId);" +
                "END" +
                "$$;";
        ps = connection.prepareStatement(query);
        ps.setString(1, username);
        ps.setString(2, docTitle);
        ps.setString(3, content);
        ps.setString(4, teamName);
        ps.setString(5, hackTitle);
        ps.setString(6, location);
        ps.executeUpdate();
    }

    @Override
    public void gradeTeam(String username, String teamName, String title, String location, int value) throws SQLException {
        PreparedStatement ps;
        String query = "DO $$ " +
                "DECLARE " +
                "   judgeId Giudice.id_giudice%TYPE; " +
                "   teamId Team.id_team%TYPE; " +
                "BEGIN " +
                "   SELECT id_giudice INTO judgeId " +
                "   FROM Giudice " +
                "   WHERE username = ?; " +
                "   SELECT id_team INTO teamId " +
                "   FROM Team NATURAL JOIN Hackathon " +
                "   WHERE nome = ? AND titolo = ? AND sede = ?; " +
                "   CALL grade_team(judgeId, teamId, ?); " +
                "END " +
                "$$;";
        ps = connection.prepareStatement(query);
        ps.setString(1, username);
        ps.setString(2, teamName);
        ps.setString(3, title);
        ps.setString(4, location);
        ps.setInt(5, value);
        ps.executeUpdate();
    }

    @Override
    public void getHackathons(String username, ArrayList<String> titles, ArrayList<String> locations) throws SQLException {
        PreparedStatement ps;
        String query = "SELECT titolo, sede " +
                        "FROM Hackathon NATURAL JOIN Selezione " +
                        "WHERE id_giudice = (SELECT id_giudice " +
                                            "FROM Giudice " +
                                            "WHERE username = ?);";
        ps = connection.prepareStatement(query);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            titles.add(rs.getString("titolo"));
            locations.add(rs.getString("sede"));
        }
    }

    @Override
    public void getTeams(String title, String location, ArrayList<String> teamNames) throws SQLException {
        PreparedStatement ps;
        String query = "SELECT nome " +
                        "FROM Team NATURAL JOIN Hackathon " +
                        "WHERE titolo = ? AND sede = ?;";
        ps = connection.prepareStatement(query);
        ps.setString(1, title);
        ps.setString(2, location);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            teamNames.add(rs.getString("nome"));
        }
    }

    @Override
    public void getDocuments(String teamName, String hackTitle, String location,
                             ArrayList<String> docTitles, ArrayList<String> contents, ArrayList<String> comments) throws SQLException {
        PreparedStatement ps;
        String query = "SELECT d.titolo, d.contenuto, d.commento " +
                        "FROM Documento d NATURAL JOIN Team t JOIN Hackathon h ON t.id_hackathon = h.id_hackathon " +
                        "WHERE t.nome = ? AND h.titolo = ? AND h.sede = ?; ";
        ps = connection.prepareStatement(query);
        ps.setString(1, teamName);
        ps.setString(2, hackTitle);
        ps.setString(3, location);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            docTitles.add(rs.getString("titolo"));
            contents.add(rs.getString("contenuto"));
            comments.add(rs.getString("commento"));
        }
    }
}
