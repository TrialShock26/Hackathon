package controller; //TODO javadoc

import dao.PlayerDAO;
import postgresImplementationDao.PlayerImplementationDAO;
import model.Hackathon;
import model.Player;
import model.Team;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Controller player.
 */
public class ControllerPlayer {
    private Controller controller;
    private ArrayList<Hackathon> myHackathons;
    private ArrayList<Team> teamsInController;

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
    public void controllerGetHackathons(String username, List<String> titles, List<String> locations,
                                        List<String> teamNames,boolean refreshing) throws SQLException{

            if(controller.getPlayer().getTeams().isEmpty() || myHackathons == null || refreshing) {

                myHackathons = new ArrayList<>();

                PlayerDAO player = new PlayerImplementationDAO();

                player.getHackathons(username, titles, locations, teamNames);

                for (int i = 0; i < locations.size(); i++) {
                    myHackathons.add(new Hackathon(titles.get(i), locations.get(i), 0,
                            null, null, null, null, 0, 0, null));

                    controller.getPlayer().getTeams().add(new Team(teamNames.get(i), null, null));

                }

            } else{
                for(int i = 0; i < myHackathons.size(); i++){
                    titles.add(myHackathons.get(i).getTitle());
                    locations.add(myHackathons.get(i).getLocation());
                    teamNames.add(controller.getPlayer().getTeams().get(i).getName());
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
                                        List<String> teamNames) throws SQLException {
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
            //controller.getPlayer().joinTeam() ??
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
                                       List<String> names, List<String> surnames) throws SQLException{

        if(teamsInController == null){

            System.out.println("DEBUG - Carico DB: " + teamName);

            for(Team t : controller.getPlayer().getTeams()){
                if(t.getName().equals(teamName)){
                    PlayerDAO playerDAO = new PlayerImplementationDAO();
                    playerDAO.getTeammates(username, teamName, title, location, names, surnames);
                    for (int i = 0; i < names.size(); i++) {
                        Player p = new Player(null,null,names.get(i),surnames.get(i));
                        t.setPlayer(p);
                    }
                    teamsInController.add(t);
                    System.out.println("DEBUG - Aggiungo alla cache" + teamName);

                }
            }

        } else {

            boolean cached = false;

            surnames.clear();
            names.clear();

            Team currteam = new Team(null,null,null);

            for(Team t : teamsInController) {
                if(t.getName().equals(teamName)){
                    cached = true;
                    currteam = t;
                }
            }

            if(cached){

                System.out.println("DEBUG - Carico CACHE: " + teamName);

                for(Player p : currteam.getPlayers()){
                    names.add(p.getName());
                    surnames.add(p.getSurname());
                }
            }else{
                System.out.println("DEBUG - Carico DB: " + teamName);

                teamsInController.add(currteam);
                PlayerDAO playerDAO = new PlayerImplementationDAO();
                playerDAO.getTeammates(username, teamName, title, location, names, surnames);
                for(int i = 0; i < names.size(); i++){
                    currteam.setPlayer(new Player(null,null,names.get(i),surnames.get(i)));
                }

                System.out.println("DEBUG - Aggiungo alla cache" + currteam.getName());

            }

        }

        System.out.println("STAMPA CACHE ------ \n");

        for(Team t: teamsInController){
            System.out.println(t.getName());
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