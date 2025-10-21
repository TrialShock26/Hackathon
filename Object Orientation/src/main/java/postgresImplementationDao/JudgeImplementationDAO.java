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
        String query = "{ CALL publish_problem((SELECT id_hackathon" +
                                                "FROM Hackathon" +
                                                "WHERE titolo = ? AND sede = ?), ?) }";
        cs = connection.prepareCall(query);
        cs.setString(1, title);
        cs.setString(2, location);
        cs.setString(3, text);
        cs.execute();
    }

    @Override
    public void examineDocument(String username, String docTitle, String content, String teamName, String hackTitle, String location) throws SQLException {
        PreparedStatement ps;
        String query = "INSERT INTO Esaminazione VALUES ((SELECT id_giudice " +
                                                         "FROM Giudice " +
                                                         "WHERE username = ?), (SELECT d.id_documento " +
                                                                                "FROM Documento d NATURAL JOIN Team t " +
                                                                                    "JOIN Hackathon h ON t.id_hackathon = h.id_hackathon " +
                                                                                "WHERE d.titolo = ? AND d.contenuto = ? " +
                                                                                    "AND t.nome = ? AND h.titolo = ? AND h.sede = ?));";
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
        CallableStatement cs;
        String query = "{ CALL grade_team((SELECT id_giudice " +
                                            "FROM Giudice " +
                                            "WHERE username = ?), (SELECT id_team " +
                                                                    "FROM Team NATURAL JOIN Hackathon " +
                                                                    "WHERE nome = ? AND titolo = ? AND sede = ?), ?) }";
        cs = connection.prepareCall(query);
        cs.setString(1, username);
        cs.setString(2, teamName);
        cs.setString(3, title);
        cs.setString(4, location);
        cs.setInt(5, value);
        cs.execute();
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
        rs.close();
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
        rs.close();
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
        rs.close();
    }
}
