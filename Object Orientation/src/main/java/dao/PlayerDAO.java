package dao;

import java.sql.SQLException;
import java.util.ArrayList;

public interface PlayerDAO {
    void subscribe(String username, String title, String location) throws SQLException;
    void joinTeam(String username, String teamName, String title, String location)  throws SQLException;
    void getHackathons(String username, ArrayList<String> titles, ArrayList<String> locationss, ArrayList<String> teamNames) throws SQLException;
    void getTeammates(String username, String teamName, String title, String location, ArrayList<String> names, ArrayList<String> surnames) throws SQLException;
}