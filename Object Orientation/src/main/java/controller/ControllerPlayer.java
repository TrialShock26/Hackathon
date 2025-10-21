package controller;

import java.util.ArrayList;
import dao.PlayerDAO;
import postgresImplementationDao.PlayerImplementationDAO;

public class ControllerPlayer {

    public void controllerGetHackathons(String username, ArrayList<String> titles, ArrayList<String> locations,
                                        ArrayList<String> teamNames) {
        try {
            PlayerDAO player = new PlayerImplementationDAO();

            player.getHackathons(username,titles,locations,teamNames);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void controllerGetOtherTeams(String username, String title, String location,
                                        ArrayList<String> teamNames) {
        try {
            PlayerDAO player = new PlayerImplementationDAO();

            player.getOtherTeams(username,title,location,teamNames);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void controllerJoinTeam(String username, String teamName, String title, String location) {

        try{

            PlayerDAO player = new PlayerImplementationDAO();

            player.joinTeam(username,teamName,title,location);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void controllerGetTeammates(String username, String teamName, String title, String location,
                                   ArrayList<String> names, ArrayList<String> surnames){

        try{
            PlayerDAO player = new PlayerImplementationDAO();

            player.getTeammates(username,teamName,title,location,names,surnames);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
