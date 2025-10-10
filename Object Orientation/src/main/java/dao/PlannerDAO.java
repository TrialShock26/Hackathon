package dao;

import java.sql.Date;
import java.util.ArrayList;

public interface PlannerDAO {
    void openHackathon(String title, String location, Date startDate, Date endDate,
                       Date startSubDate, Date endSubDate, int maxPlayers, int maxTeamDim,
                       String planUsername, String judgesUsernames);
    void startHackathon(String title, String location);
    void endHackathon(String title, String location, ArrayList<String> teamNames, ArrayList<Integer> finalScore);
    void getHackathons(String username, ArrayList<String> title, ArrayList<String> location);
    void getUsers(String planUser,
                  ArrayList<String> allUsernames,
                  ArrayList<String> allNames,
                  ArrayList<String> allSurnames,
                  ArrayList<String> allPasswords);
}