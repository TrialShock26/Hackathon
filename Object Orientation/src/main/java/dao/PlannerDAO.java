package dao;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

public interface PlannerDAO {
    void openHackathon(String title, String location, Date startDate, Date endDate,
                       Date startSubDate, Date endSubDate, int maxPlayers, int maxTeamDim,
                       String planUsername, String judgesUsernames) throws SQLException;
    void startHackathon(String title, String location) throws SQLException;
    void endHackathon(String title, String location, ArrayList<String> teamNames, ArrayList<Double> finalScores) throws SQLException;
    void getHackathons(String username, ArrayList<String> titles, ArrayList<String> locations,
                       ArrayList<Date> startDate, ArrayList<Date> endDate,
                       ArrayList<Date> startSubDate, ArrayList<Date> endSubDate,
                       ArrayList<Integer> maxPlayers, ArrayList<Integer> maxTeamDim) throws SQLException;
    void getUsers(String planUser,
                  ArrayList<String> allUsernames,
                  ArrayList<String> allNames,
                  ArrayList<String> allSurnames,
                  ArrayList<String> allPasswords) throws SQLException;
}