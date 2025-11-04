package controller; //TODO javadoc

import dao.PlayerDAO;
import postgresImplementationDao.PlayerImplementationDAO;
import model.Hackathon;
import model.Player;
import model.Team;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * The type Controller player.
 */
public class ControllerPlayer {
    private Controller controller;
    private ArrayList<Hackathon> myHackathons;
    private ArrayList<Team> myTeams;
    private ArrayList<Player> myTeamMates;

    /**
     * Instantiates a new Controller player.
     *
     * @param controller the controller
     */
    public ControllerPlayer(Controller controller) {this.controller = controller;}

    /**
     * Controller get hackathons.
     *
     * @param username   the username
     * @param titles     the titles
     * @param locations  the locations
     * @param teamNames  the team names
     * @param refreshing the refreshing
     * @throws SQLException the sql exception
     */
    public void controllerGetHackathons(String username, ArrayList<String> titles, ArrayList<String> locations,
                                        ArrayList<String> teamNames,boolean refreshing) throws SQLException{

            if(myTeams == null || myHackathons == null || refreshing) {

                myTeams = new ArrayList<Team>();
                myHackathons = new ArrayList<Hackathon>();

                PlayerDAO player = new PlayerImplementationDAO();

                player.getHackathons(username, titles, locations, teamNames);

                for (int i = 0; i < locations.size(); i++) {
                    myHackathons.add(new Hackathon(titles.get(i), locations.get(i), 0,
                            null, null, null, null, 0, 0, null));

                    myTeams.add(new Team(teamNames.get(i), null, null));

                }

            } else{
                for(int i = 0; i < myHackathons.size(); i++){
                    titles.add(myHackathons.get(i).getTitle());
                    locations.add(myHackathons.get(i).getLocation());
                    teamNames.add(myTeams.get(i).getName());
                }
            }

    }

    /**
     * Controller get other teams.
     *
     * @param username  the username
     * @param title     the title
     * @param location  the location
     * @param teamNames the team names
     * @throws SQLException the sql exception
     */
    public void controllerGetOtherTeams(String username, String title, String location,
                                        ArrayList<String> teamNames) throws SQLException {
            PlayerDAO player = new PlayerImplementationDAO();
            player.getOtherTeams(username,title,location,teamNames);
    }

    /**
     * Controller join team.
     *
     * @param username the username
     * @param teamName the team name
     * @param title    the title
     * @param location the location
     * @throws SQLException the sql exception
     */
    public void controllerJoinTeam(String username, String teamName, String title, String location) throws SQLException {
            PlayerDAO player = new PlayerImplementationDAO();
            player.joinTeam(username,teamName,title,location);
    }

    /**
     * Controller get teammates.
     *
     * @param username the username
     * @param teamName the team name
     * @param title    the title
     * @param location the location
     * @param names    the names
     * @param surnames the surnames
     * @throws SQLException the sql exception
     */
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

    /**
     * Subscribe.
     *
     * @param username the username
     * @param title    the title
     * @param location the location
     * @throws SQLException the sql exception
     */
    public void subscribe(String username, String title, String location) throws SQLException {
        PlayerDAO player = new PlayerImplementationDAO();
        player.subscribe(username, title, location);
        controller.getPlayer().signUpHackathon(controller.getControllerHackathon().getAvailableHackathon(title, location));
    }
}