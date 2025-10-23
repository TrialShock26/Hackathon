package postgresImplementationDao;

import dao.PlannerDAO;
import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;

public class PlannerImplementationDAO implements PlannerDAO {
    private Connection connection;

    public PlannerImplementationDAO() {
        try {
            connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void openHackathon(String title, String location, Date startDate, Date endDate,
                              Date startSubDate, Date endSubDate, int maxPlayers, int maxTeamDim,
                              String planUsername, String judgesUsernames) throws SQLException {
        CallableStatement query;
        query = connection.prepareCall("CALL add_hackathon(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        query.setString(1, title);
        query.setString(2, location);
        query.setDate(3, startDate);
        query.setDate(4, endDate);
        query.setDate(5, startSubDate);
        query.setDate(6, endSubDate);
        query.setInt(7, maxPlayers);
        query.setInt(8, maxTeamDim);
        query.setString(9, planUsername);
        query.setString(10, judgesUsernames);
        query.execute();
    }

    @Override
    public void startHackathon(String title, String location) throws SQLException {
        CallableStatement cs;
        String query = "{ CALL start_hackathon((SELECT id_hackathon " +
                                                "FROM Hackathon " +
                                                "WHERE titolo = ? AND sede = ?)) }";
        cs = connection.prepareCall(query);
        cs.setString(1, title);
        cs.setString(2, location);
        cs.execute();
    }

    @Override
    public void endHackathon(String title, String location, ArrayList<String> teamNames, ArrayList<Double> finalScores) throws SQLException {
        connection.setAutoCommit(false);
        CallableStatement cs;
        ResultSet rs;
        String query = "{ ? = CALL end_hackathon((SELECT id_hackathon " +
                                                    "FROM Hackathon " +
                                                    "WHERE titolo = ? AND sede = ?)) }";
        cs = connection.prepareCall(query);
        cs.registerOutParameter(1, Types.OTHER);
        cs.setString(2, title);
        cs.setString(3, location);
        cs.execute();
        rs = (ResultSet) cs.getObject(1);

        while (rs.next()) {
            teamNames.add(rs.getString("nome_team"));
            finalScores.add(rs.getDouble("voto_finale"));
        }
        rs.close();
        connection.commit();
        connection.setAutoCommit(true);
    }

    @Override
    public void getHackathons(String username, ArrayList<String> titles, ArrayList<String> locations,ArrayList<Date> startDates,
                              ArrayList<Date> endDates,ArrayList<Date> startSubDate,ArrayList<Date> endSubDate,
                              ArrayList<Integer> maxPlayers, ArrayList<Integer> maxTeamDim) throws SQLException {
        PreparedStatement ps;
        String query = "SELECT titolo, sede , durata, data_inizio, data_fine, descrizione_problema, " +
                "data_apertura_iscrizioni, data_chiusura_iscrizioni, max_iscritti, max_dim_team " +
                "FROM Hackathon " +
                "WHERE id_organizzatore = (SELECT id_organizzatore " +
                                            "FROM Organizzatore " +
                                            "WHERE username = ?);";
        ps = connection.prepareStatement(query);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            titles.add(rs.getString("titolo"));
            locations.add(rs.getString("sede"));
            startDates.add(rs.getDate("data_inizio"));
            endDates.add(rs.getDate("data_fine"));
            startSubDate.add(rs.getDate("data_apertura_iscrizioni"));
            maxPlayers.add(rs.getInt("max_iscritti"));
            maxTeamDim.add(rs.getInt("max_team"));

        }
        rs.close();
    }

    @Override
    public void getUsers(String planUser,
                         ArrayList<String> allUsernames,
                         ArrayList<String> allNames,
                         ArrayList<String> allSurnames,
                         ArrayList<String> allPasswords) throws SQLException {
        PreparedStatement ps;
        String query = "SELECT * " +
                       "FROM Utente " +
                       "WHERE username <> ? AND username <> 'username';";
        ps = connection.prepareStatement(query);
        ps.setString(1, planUser);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            allUsernames.add(rs.getString("username"));
            allNames.add(rs.getString("nome"));
            allSurnames.add(rs.getString("cognome"));
            allPasswords.add(rs.getString("password"));
        }
        rs.close();
    }
}
