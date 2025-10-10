package dao;

import java.util.ArrayList;

public interface PlayerDAO {
    void subscribe(String username, String title, String location);
    void joinTeam(String username, String teamName, String title, String location);
    void getHackathons(String username, ArrayList<String> title, ArrayList<String> location, ArrayList<String> teamNames);
    void getTeammates(String username, String teamName, String title, String location, ArrayList<String> names, ArrayList<String> surnames);
}