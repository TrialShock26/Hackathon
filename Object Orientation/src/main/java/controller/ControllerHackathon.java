package controller; //TODO javadoc

import dao.HackathonDAO;
import dao.UserDAO;
import postgresImplementationDao.HackathonImplementationDAO;
import postgresImplementationDao.UserImplementationDAO;
import model.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.*;

/**
 * The type Controller hackathon.
 */
public class ControllerHackathon {
    private Controller controller;
    private ArrayList<Hackathon> availableHackathons;
    private ArrayList<Hackathon> closedHackathons;
    private ArrayList<ArrayList<Object>> myScoreboard;
    private ArrayList<Team> myOverall;
    private ArrayList<Double> myOverallScores;


    public ControllerHackathon(Controller controller) {this.controller = controller;}

    /**
     * Controller overall ranking.
     *
     * @param teamNames  the team names
     * @param scores     the scores
     * @param title    the title
     * @param location  the location
     * @param refreshing the refreshing
     * @throws SQLException the sql exception
     */

    public void controllerScoreboard(String title, String location,
                                     ArrayList<String> teamNames, ArrayList<Double> scores,
                                     boolean refreshing) throws SQLException {

        if (myScoreboard == null || refreshing) {

            myScoreboard = new ArrayList<>();

            System.out.println(title);

            HackathonDAO hackathonDAO = new HackathonImplementationDAO();
            hackathonDAO.scoreboard(title, location, teamNames, scores);

            for (int i = 0; i < teamNames.size(); i++) {
                Team newTeam = new Team(
                        teamNames.get(i),
                        controller.getPlayer(),
                        new Hackathon(title, location, 0, null, null, null, null, 0, 0, null)
                );

                ArrayList<Object> pair = new ArrayList<>();
                pair.add(newTeam);       // indice 0 → Team
                pair.add(scores.get(i)); // indice 1 → punteggio Double

                myScoreboard.add(pair);
            }

        } else {

            teamNames.clear();
            scores.clear();

            boolean isCached = false;

            for (ArrayList<Object> pair : myScoreboard) {
                Team t = (Team) pair.get(0);
                if (t.getHackathon().getTitle().equals(title) &&
                        t.getHackathon().getLocation().equals(location)) {
                    isCached = true;
                }
            }

            if (isCached) {
                for (ArrayList<Object> pair : myScoreboard) {
                    Team t = (Team) pair.get(0);
                    double score = (double) pair.get(1);

                    if (t.getHackathon().getTitle().equals(title) &&
                            t.getHackathon().getLocation().equals(location)) {
                        teamNames.add(t.getName());
                        scores.add(score);
                    }
                }

            } else {

                HackathonDAO hackathonDAO = new HackathonImplementationDAO();
                hackathonDAO.scoreboard(title, location, teamNames, scores);

                for (int i = 0; i < teamNames.size(); i++) {
                    String daoTeamName = teamNames.get(i);
                    boolean exists = false;

                    for (ArrayList<Object> pair : myScoreboard) {
                        Team t = (Team) pair.get(0);
                        if (t.getName().equals(daoTeamName) &&
                                t.getHackathon().getTitle().equals(title) &&
                                t.getHackathon().getLocation().equals(location)) {
                            exists = true;
                        }
                    }

                    if (!exists) {
                        Team newTeam = new Team(
                                daoTeamName,
                                controller.getPlayer(),
                                new Hackathon(title, location, 0, null, null, null, null, 0, 0, null)
                        );

                        ArrayList<Object> pair = new ArrayList<>();
                        pair.add(newTeam);
                        pair.add(scores.get(i));

                        myScoreboard.add(pair);
                    }
                }
            }
        }
    }


    public void controllerOverallRanking(List<String> teamNames, List<Double> scores,
                                         List<String> titles, List<String> locations,
                                         boolean refreshing) throws SQLException {

        if(myOverall == null || refreshing) {

            myOverall = new ArrayList<>();
            myOverallScores = new ArrayList<>();

            System.out.println("DB");

            HackathonDAO hackathonDAO = new HackathonImplementationDAO();
            hackathonDAO.overallRanking(teamNames,scores,titles,locations);

            for (int i = 0; i < teamNames.size(); i++) {
                Team newTeam = new Team(
                        teamNames.get(i),
                        controller.getPlayer(),
                        new Hackathon(titles.get(i), locations.get(i), 0, null, null, null, null, 0, 0, null)
                );
                myOverall.add(newTeam);
                myOverallScores.add(scores.get(i));
            }


        }else{

            System.out.println("CACHE");

            teamNames.clear();
            scores.clear();
            titles.clear();
            locations.clear();

            for(int i = 0; i < myOverall.size(); i++) {
                teamNames.add(myOverall.get(i).getName());
                scores.add(myOverallScores.get(i));
                titles.add(myOverall.get(i).getHackathon().getTitle());
                locations.add(myOverall.get(i).getHackathon().getLocation());
            }

        }


        /*System.out.println("=== controllerOverallRanking ===");
        ArrayList<String> addedTeams = new ArrayList<>();

        if (controller.getPlayer().getTeams().isEmpty() || refreshing) {
            System.out.println("Cache vuota o refresh richiesto, caricamento dal DB...");

            HackathonDAO hackathonDAO = new HackathonImplementationDAO();
            hackathonDAO.overallRanking(teamNames, scores, titles, locations);

            for (int i = 0; i < teamNames.size(); i++) {
                String teamName = teamNames.get(i);
                String title = titles.get(i);
                String location = locations.get(i);
                double score = scores.get(i);

                boolean exists = false;
                for (Team t : controller.getPlayer().getTeams()) {
                    if (t.getName().equals(teamName) &&
                            t.getHackathon().getTitle().equals(title) &&
                            t.getHackathon().getLocation().equals(location)) {
                        exists = true;
                    }
                }

                if (!exists) {
                    Team t = new Team(teamName, controller.getPlayer(),
                            new Hackathon(title, location, 0, null, null, null, null, 0, 0, null));
                    controller.getPlayer().getTeams().add(t);

                    System.out.println("Aggiunto Team ranking: " + teamName + " -> " + score);
                } else {
                    System.out.println("Team già presente: " + teamName);
                }
            }

        } else {
            System.out.println("Caricamento dati dalla cache per overall ranking...");
            teamNames.clear();
            scores.clear();
            titles.clear();
            locations.clear();

            for (Team t : controller.getPlayer().getTeams()) {
                boolean alreadyAdded = false;
                for (String added : addedTeams) {
                    if (added.equals(t.getName())) {
                        alreadyAdded = true;
                    }
                }

                if (!alreadyAdded) {
                    teamNames.add(t.getName());
                    titles.add(t.getHackathon().getTitle());
                    locations.add(t.getHackathon().getLocation());

                    for (ArrayList<String> pair : teamScorePairs) {
                        if (pair.get(0).equals(t.getName())) {
                            scores.add(Double.parseDouble(pair.get(1)));
                        }
                    }

                    addedTeams.add(t.getName());
                    System.out.println("Caricato dalla cache ranking: " + t.getName() + " -> " + scores.get(scores.size()-1));
                }
            }
        }*/
    }

/**
     * Controller get available hackathons.
     *
     * @param titles        the titles
     * @param locations     the locations
     * @param periodsOfTime the periods of time
     * @param startDates    the start dates
     * @param endDates      the end dates
     * @param startSubDates the start sub dates
     * @param endSubDates   the end sub dates
     * @param maxPlayers    the max players
     * @param maxTeamDim    the max team dim
     * @param refreshing    the refreshing
     * @throws SQLException the sql exception
     */

    public void controllerGetAvailableHackathons(List<String> titles, List<String> locations, List<Integer> periodsOfTime,
                                                   List<Date> startDates, List<Date> endDates, List<Date> startSubDates,
                                                   List<Date> endSubDates, List<Integer> maxPlayers, List<Integer> maxTeamDim,
                                                   boolean refreshing) throws SQLException {
        if (availableHackathons == null || refreshing) {
            UserDAO user = new UserImplementationDAO();
            user.getHackathons(titles, locations, periodsOfTime, startDates, endDates, startSubDates, endSubDates, maxPlayers, maxTeamDim);
            availableHackathons = new ArrayList<>();
            for (int i = 0; i < titles.size(); i++) {
                availableHackathons.add(new Hackathon(titles.get(i), locations.get(i), periodsOfTime.get(i).longValue(), startDates.get(i),
                        endDates.get(i), startSubDates.get(i), endSubDates.get(i), maxPlayers.get(i), maxTeamDim.get(i), null));
            }
        } else {
            for (Hackathon availableHackathon : availableHackathons) {
                titles.add(availableHackathon.getTitle());
                locations.add(availableHackathon.getLocation());
                periodsOfTime.add((int)availableHackathon.getPeriodOfTime());
                startDates.add(availableHackathon.getStartDate());
                endDates.add(availableHackathon.getEndDate());
                startSubDates.add(availableHackathon.getStartSubscriptionDate());
                endSubDates.add(availableHackathon.getEndSubscriptionDate());
                maxPlayers.add(availableHackathon.getMaxPlayers());
                maxTeamDim.add(availableHackathon.getMaxTeamDim());
            }
        }
    }

    /**
     * Controller get closed hackathons.
     *
     * @param titles     the titles
     * @param locations  the locations
     * @param refreshing the refreshing
     * @throws SQLException the sql exception
     */
    public void controllerGetClosedHackathons(List<String> titles, List<String> locations, boolean refreshing) throws SQLException{

        if(closedHackathons==null || refreshing){

            closedHackathons = new ArrayList<>();
            HackathonDAO hackathon = new HackathonImplementationDAO();
            hackathon.getClosedHackathons(titles, locations);
            for(int i = 0; i < titles.size(); i++){
                closedHackathons.add(new Hackathon(titles.get(i),locations.get(i),
                        0,null,null,null,null,0,0,null));
            }
        }else{
            for(Hackathon hackathon : closedHackathons){
                titles.add(hackathon.getTitle());
                locations.add(hackathon.getLocation());
            }
        }
    }

    /**
     * Gets available hackathon.
     *
     * @param title    the title
     * @param location the location
     * @return the available hackathon
     */
    public Hackathon getAvailableHackathon(String title, String location) {
        for (Hackathon h : availableHackathons) {
            if (h.getTitle().equals(title) && h.getLocation().equals(location)) {return h;}
        }
        return null;
    }
}
