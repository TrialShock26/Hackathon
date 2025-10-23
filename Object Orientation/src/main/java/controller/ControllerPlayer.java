package controller;

import java.sql.SQLException;
import java.util.ArrayList;
import dao.PlayerDAO;
import model.Hackathon;
import model.Player;
import model.Team;
import postgresImplementationDao.PlayerImplementationDAO;

public class ControllerPlayer {

    private ArrayList<Hackathon> myHackathons;
    private ArrayList<Team> myTeams;
    private ArrayList<Player> myTeamMates;

    public void controllerGetHackathons(String username, ArrayList<String> titles, ArrayList<String> locations,
                                        ArrayList<String> teamNames) throws SQLException{

            if(myTeams == null || myHackathons == null) {

                myTeams = new ArrayList<Team>();
                myHackathons = new ArrayList<Hackathon>();

                PlayerDAO player = new PlayerImplementationDAO();

                player.getHackathons(username, titles, locations, teamNames);

                for (int i = 0; i < locations.size(); i++) {
                    myHackathons.add(new Hackathon(titles.get(i), locations.get(i), 0,
                            null, null, null, null, 0, 0, null));

                    myTeams.add(new Team(teamNames.get(i), null, null));

                }

            }else{
                for(int i = 0; i < myHackathons.size(); i++){
                    titles.add(myHackathons.get(i).getTitle());
                    locations.add(myHackathons.get(i).getLocation());
                    teamNames.add(myTeams.get(i).getName());
                }
            }

    }

    public void controllerGetOtherTeams(String username, String title, String location,
                                        ArrayList<String> teamNames) throws SQLException {

            PlayerDAO player = new PlayerImplementationDAO();

            player.getOtherTeams(username,title,location,teamNames);

    }

    public void controllerJoinTeam(String username, String teamName, String title, String location) throws SQLException {

            PlayerDAO player = new PlayerImplementationDAO();

            player.joinTeam(username,teamName,title,location);

    }

    public void controllerGetTeammates(String username, String teamName, String title, String location,
                                   ArrayList<String> names, ArrayList<String> surnames) throws SQLException{

        if(myTeamMates == null) {

            myTeamMates = new ArrayList<>();

            PlayerDAO player = new PlayerImplementationDAO();

            player.getTeammates(username,teamName,title,location,names,surnames);

            for (int i = 0; i < names.size(); i++) {
                myTeamMates.add(new Player(null,null,names.get(i),surnames.get(i)));
            }
        }else {
            for(int i = 0; i < myTeamMates.size(); i++){
                names.add(myTeamMates.get(i).getName());
                surnames.add(myTeamMates.get(i).getSurname());
            }
        }
    }

    public void subscribe(String username, String title, String location) throws SQLException {
        PlayerDAO player = new PlayerImplementationDAO();
        player.subscribe(username, title, location);
    }


}
