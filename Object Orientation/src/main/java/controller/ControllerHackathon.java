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
    private ArrayList<Hackathon> availableHackathons;
    private ArrayList<Hackathon> closedHackathons;
    private ArrayList<Team> myScoreboard;
    private ArrayList<Double> myScores;
    private ArrayList<Team> myRanking;
    private ArrayList<Double> myRankingScores;
    private String lastRankingTitle;
    private ArrayList<Hackathon> overallHackathons;

    /**
     * Controller overall ranking.
     *
     * @param teamNames  the team names
     * @param scores     the scores
     * @param titles     the titles
     * @param locations  the locations
     * @param refreshing the refreshing
     * @throws SQLException the sql exception
     */
    public void controllerOverallRanking(ArrayList<String> teamNames, ArrayList<Double> scores,
                                         ArrayList<String> titles, ArrayList<String> locations, boolean refreshing) throws SQLException {

            if(myScoreboard == null || refreshing) {

                myScoreboard = new ArrayList<>();
                myScores = new ArrayList<>();
                overallHackathons = new ArrayList<>();
                HackathonDAO hackathon = new HackathonImplementationDAO();
                hackathon.overallRanking(teamNames,scores,titles,locations);

                for(int i = 0; i < teamNames.size(); i++){
                    myScoreboard.add(new Team(teamNames.get(i),null,null));
                    myScores.add(scores.get(i));
                    overallHackathons.add(new Hackathon(titles.get(i),locations.get(i),0,null,null,
                            null,null,0,0,null));
                }

            } else {
                for (int i = 0; i < myScoreboard.size(); i++) {
                    teamNames.add(myScoreboard.get(i).getName());
                    scores.add(myScores.get(i));
                    titles.add(overallHackathons.get(i).getTitle());
                    locations.add(overallHackathons.get(i).getLocation());

                }
            }
    }

    /**
     * Controller scoreboard.
     *
     * @param title      the title
     * @param location   the location
     * @param teamNames  the team names
     * @param scores     the scores
     * @param refreshing the refreshing
     * @throws SQLException the sql exception
     */
    public void controllerScoreboard(String title, String location,
                                  ArrayList<String> teamNames, ArrayList<Double> scores,
                                  boolean refreshing) throws SQLException {
        if (myRanking == null || refreshing || !title.equals(lastRankingTitle)) {
            myRanking = new ArrayList<>();
            myRankingScores = new ArrayList<>();
            lastRankingTitle = title;

            HackathonDAO hackathonDAO = new HackathonImplementationDAO();
            hackathonDAO.scoreboard(title, location, teamNames, scores);

            for (int i = 0; i < teamNames.size(); i++) {
                myRanking.add(new Team(teamNames.get(i), null, null));
                myRankingScores.add(scores.get(i));
            }

        } else {
            teamNames.clear();
            scores.clear();

            for (int i = 0; i < myRanking.size(); i++) {
                teamNames.add(myRanking.get(i).getName());
                scores.add(myRankingScores.get(i));
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
    public void controllerGetAvailableHackathons(ArrayList<String> titles, ArrayList<String> locations, ArrayList<Integer> periodsOfTime,
                                                   ArrayList<Date> startDates, ArrayList<Date> endDates, ArrayList<Date> startSubDates,
                                                   ArrayList<Date> endSubDates, ArrayList<Integer> maxPlayers, ArrayList<Integer> maxTeamDim,
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
    public void controllerGetClosedHackathons(ArrayList<String> titles, ArrayList<String> locations, boolean refreshing) throws SQLException{

        if(closedHackathons==null || refreshing){

            closedHackathons = new ArrayList<>();
            HackathonDAO hackathon = new HackathonImplementationDAO();
            hackathon.getClosedHackathons(titles,locations);
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
