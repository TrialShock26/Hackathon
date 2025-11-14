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


        if(myHackathons == null || refreshing) {
            //if(controller.getPlayer().getTeams().isEmpty() || myHackathons == null || refreshing) {

                myHackathons = new ArrayList<>();

                PlayerDAO player = new PlayerImplementationDAO();

                player.getHackathons(username, titles, locations, teamNames);

                //ottengo i team con i rispettivi hackathon in cui gioco


                for (int i = 0; i < locations.size(); i++) {

                    //aggiungo gli hackathon ai quali gioco

                    Hackathon hack = new Hackathon(titles.get(i), locations.get(i));

                    Team newTeam = new Team(teamNames.get(i),
                            controller.getPlayer(),
                            hack);

                    hack.setTeam(newTeam);

                    myHackathons.add(hack);

                    //aggiungo i team a cui gioco (legati agli hackathon)

                    //System.out.println(newTeam.getName());

                    //controller.getPlayer().getTeams();

                }

//                for(Team t : controller.getPlayer().getTeams()){
//                    System.out.println(t.getName());
//                }

//                for(Hackathon h : myHackathons){
//                    System.out.println(h.getTitle());
//                }

            } else{
                for(int i = 0; i < myHackathons.size(); i++){
                    titles.add(myHackathons.get(i).getTitle());
                    locations.add(myHackathons.get(i).getLocation());
                    teamNames.add(controller.getPlayer().getTeams().get(i).getName());
                }
            }

        //verifico se ogni team in cui partecipo è stato salvato

//        for(Team t : controller.getPlayer().getTeams()){
//            System.out.println(t.getName());
//        }

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
                                       List<String> names, List<String> surnames, boolean refreshing) throws SQLException {

        //System.out.println(controller.getPlayer().getTeams());

       for(int i = 0; i < controller.getPlayer().getTeams().size(); i++) {
           if (controller.getPlayer().getTeams().get(i).getName().equals(teamName)) {
               if (controller.getPlayer().getTeams().get(i).getPlayers().size() == 1 || refreshing) {

                   System.out.println("DB");

                   //se ci sono solo io potrei dover caricare altri teamMates

                   //carico i teammates da db

                   PlayerDAO playerDAO = new PlayerImplementationDAO();
                   playerDAO.getTeammates(username, teamName, title, location, names, surnames);


                   for (int j = 0; j < names.size(); j++) {
                       controller.getPlayer().getTeams().get(i).setPlayer(
                               new Player(null, null,
                                       names.get(j),
                                       surnames.get(j))
                       );
                   }

                   //aggiungo me stesso poichè dal db non risale il nome e il cognome dell'utente stesso
                   //mentre nel model se stesso è presente

                   names.add((controller.getPlayer().getTeams().get(i).getPlayers().get(0).getName()));
                   surnames.add((controller.getPlayer().getTeams().get(i).getPlayers().get(0).getSurname()));

               } else {

                   System.out.println("CACHE");

                   for (int j = 0; j < controller.getPlayer().getTeams().get(i).getPlayers().size(); j++) {
                       System.out.println(controller.getPlayer().getTeams().get(i).getPlayers().get(j).getName());
                   }

                   names.clear();
                   surnames.clear();

                   for (int j = 0; j < controller.getPlayer().getTeams().get(i).getPlayers().size(); j++) {
                       names.add(controller.getPlayer().getTeams().get(i).getPlayers().get(j).getName());
                       surnames.add(controller.getPlayer().getTeams().get(i).getPlayers().get(j).getSurname());
                   }

               }
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