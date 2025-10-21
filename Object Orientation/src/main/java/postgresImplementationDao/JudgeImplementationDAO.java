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
                    "hackId Hackathon.id_hackathon%TYPE;" +
                "BEGIN " +
                    "SELECT id_hackathon INTO hackId " +
                    "FROM Hackathon " +
                    "WHERE titolo = '" + title + "' AND sede = '" + location + "';" +
                    "CALL publish_problem(hackId, '" + text + "');" +
                "END $$;";
        cs = connection.prepareCall(query);
        cs.execute();
    }

    @Override
    public void examineDocument(String username, String docTitle, String content,
                                String teamName, String hackTitle, String location, String text) throws SQLException {
        PreparedStatement ps;
        String query = "DO $$ " +
                "DECLARE " +
                    "judgeId Giudice.id_giudice%TYPE;" +
                    "docId Documento.id_documento%TYPE;" +
                "BEGIN " +
                    "SELECT id_giudice INTO judgeId " +
                        "FROM Giudice " +
                        "WHERE username = '" + username + "';" +
                    "SELECT d.id_documento INTO docId " +
                        "FROM Documento d NATURAL JOIN Team t " +
                            "JOIN Hackathon h ON t.id_hackathon = h.id_hackathon " +
                        "WHERE d.titolo = '" + docTitle + "' AND d.contenuto = '" + content + "' " +
                            "AND t.nome = '" + teamName + "' AND h.titolo = '" + hackTitle + "' AND h.sede = '" + location + "'; " +
                    "INSERT INTO Esaminazione VALUES (judgeId, docId); " +
                    "UPDATE Documento SET commento = '" + text + "' " +
                        "WHERE id_documento = docId; " +
                "END $$;";
        ps = connection.prepareStatement(query);
        ps.executeUpdate();
    }

    @Override
    public void gradeTeam(String username, String teamName, String title, String location, int value) throws SQLException {
        CallableStatement cs;
        String query = "DO $$ " +
                "DECLARE " +
                    "judgeId Giudice.id_giudice%TYPE;" +
                    "teamId Team.id_team%TYPE;" +
                "BEGIN " +
                    "SELECT id_giudice INTO judgeId " +
                    "FROM Giudice " +
                    "WHERE username = '" + username + "';" +
                    "SELECT id_team INTO teamId " +
                    "FROM Team NATURAL JOIN Hackathon " +
                    "WHERE nome = '" + teamName + "' AND titolo = '" + title + "' AND sede = '" + location + "';" +
                    "CALL grade_team(judgeId, teamId, " + value + ");" +
                "END $$;";
        cs = connection.prepareCall(query);
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
