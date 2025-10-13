package postgresImplementationDao;

import dao.UserDAO;
import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;

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
    public boolean login(String username, String password) throws SQLException {
        boolean isRegistered = false;
        PreparedStatement query;
        query = connection.prepareStatement("SELECT * FROM Utente WHERE username = ? AND password = ?");
        query.setString(1, username);
        query.setString(2, password);
        ResultSet rs = query.executeQuery();
        if (rs.next()) {isRegistered = true;}
        return isRegistered;
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

    @Override
    public void getHackathons(ArrayList<String> titles, ArrayList<String> locations, ArrayList<Integer> periodsOfTime,
                              ArrayList<Date> startDates, ArrayList<Date> endDates, ArrayList<Date> startSubDates, ArrayList<Date> endSubDates,
                              ArrayList<Integer> maxPlayers, ArrayList<Integer> maxTeamDim) throws SQLException {
        PreparedStatement ps;
        String query = "SELECT titolo, sede, durata, data_inizio, data_fine, " +
                            "data_apertura_iscrizioni, data_chiusura_iscrizioni, " +
                            "max_iscritti, max_dim_team " +
                        "FROM Hackathon" +
                        "WHERE data_chiusura_iscrizioni > CURRENT_DATE; ";
        ps = connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            titles.add(rs.getString("titolo"));
            locations.add(rs.getString("sede"));
            periodsOfTime.add(rs.getInt("durata"));
            startDates.add(rs.getDate("data_inizio"));
            endDates.add(rs.getDate("data_fine"));
            startSubDates.add(rs.getDate("data_apertura_iscrizioni"));
            endSubDates.add(rs.getDate("data_chiusura_iscrizioni"));
            maxPlayers.add(rs.getInt("max_iscritti"));
            maxTeamDim.add(rs.getInt("max_dim_team"));
        }
    }
}
