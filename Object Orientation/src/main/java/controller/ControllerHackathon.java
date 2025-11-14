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
    private ArrayList<ArrayList<Double>> scoresMatrix;
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
                                     List<String> teamNames, List<Double> scores) throws SQLException {
        if (scoresMatrix == null) {
            scoresMatrix = new ArrayList<>();
            for (int i = 0; i < closedHackathons.size(); i++) {
                scoresMatrix.add(new ArrayList<>());
            }
        }
        Hackathon hack = null;
        int idx = -1;
        for (Hackathon h : closedHackathons) {
            if (h.getTitle().equals(title) && h.getLocation().equals(location)) {
                idx = closedHackathons.indexOf(h);
                hack = h;
            }
        }

        if (scoresMatrix.get(idx).isEmpty()) {
            HackathonDAO hackathonDAO = new HackathonImplementationDAO();
            hackathonDAO.scoreboard(title, location, teamNames, scores);

            for (int i = 0; i < teamNames.size(); i++) {
                Team newTeam = new Team(
                        teamNames.get(i),
                        null,
                        hack
                );
                hack.setTeam(newTeam);
                scoresMatrix.get(idx).add(scores.get(i));
            }
        } else {
            for (int i = 0;  i < hack.getTeams().size(); i++) {
                teamNames.add(hack.getTeams().get(i).getName());
                scores.add(scoresMatrix.get(idx).get(i));
            }
        }
    }


    public void controllerOverallRanking(List<String> teamNames, List<Double> scores,
                                         List<String> titles, List<String> locations,
                                         boolean refreshing) throws SQLException {
        if (myOverall == null || refreshing) {
            myOverall = new ArrayList<>();
            myOverallScores = new ArrayList<>();

            HackathonDAO hackathonDAO = new HackathonImplementationDAO();
            hackathonDAO.overallRanking(teamNames,scores,titles,locations);

            for (int i = 0; i < teamNames.size(); i++) {
                Team newTeam = new Team(
                        teamNames.get(i),
                        controller.getPlayer(),
                        new Hackathon(titles.get(i), locations.get(i))
                );
                myOverall.add(newTeam);
                myOverallScores.add(scores.get(i));
            }
        } else {
            for (int i = 0; i < myOverall.size(); i++) {
                teamNames.add(myOverall.get(i).getName());
                scores.add(myOverallScores.get(i));
                titles.add(myOverall.get(i).getHackathon().getTitle());
                locations.add(myOverall.get(i).getHackathon().getLocation());
            }
        }
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

        if (closedHackathons == null || refreshing) {
            closedHackathons = new ArrayList<>();
            HackathonDAO hackathon = new HackathonImplementationDAO();
            hackathon.getClosedHackathons(titles, locations);
            for(int i = 0; i < titles.size(); i++){
                closedHackathons.add(new Hackathon(titles.get(i),locations.get(i)));
            }
            scoresMatrix = null;
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