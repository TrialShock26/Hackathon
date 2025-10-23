package dao;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

public interface UserDAO {
    String login(String username, String password) throws SQLException;
    void newUser(String username, String password, String name, String surname)  throws SQLException;
    void getHackathons(ArrayList<String> titles, ArrayList<String> locations, ArrayList<Integer> periodsOfTime,
                       ArrayList<Date> startDates, ArrayList<Date> endDates, ArrayList<Date> startSubDates, ArrayList<Date> endSubDates,
                       ArrayList<Integer> maxPlayers, ArrayList<Integer> maxTeamDim) throws SQLException;
}