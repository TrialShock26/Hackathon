package controller;

import dao.HackathonDAO;
import dao.UserDAO;
import postgresImplementationDao.HackathonImplementationDAO;
import postgresImplementationDao.UserImplementationDAO;
import model.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.*;

public class ControllerHackathon {
    private ArrayList<Hackathon> availableHackathons;
    private ArrayList<Hackathon> closedHackathons;
    private ArrayList<Team> myScoreboard;
    private ArrayList<Double> myScores;
    private ArrayList<Hackathon> overallHackathons;
    private ArrayList<Team> teamsInController;
    private ArrayList<Double> scoresInController;

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

    public void controllerScoreboard(String title, String location,
                                     ArrayList<String> teamNames, ArrayList<Double> scores,
                                     boolean refreshing) throws SQLException {

        if (teamsInController == null || refreshing) {
            System.out.println("DEBUG - Carico ranking da DB: " + title + ", refresh=" + refreshing);

            HackathonDAO hackathonDAO = new HackathonImplementationDAO();
            hackathonDAO.scoreboard(title, location, teamNames, scores);
            teamsInController = new ArrayList<>();
            scoresInController = new ArrayList<>();

            for (int i = 0; i < teamNames.size(); i++) {
                Team t = new Team(teamNames.get(i), null,
                        new Hackathon(title, location, 0, null, null, null, null, 0, 0, null));
                teamsInController.add(t);
                System.out.println("DEBUG - Aggiungo alla cache" + teamsInController.get(i).getName());
                scoresInController.add(scores.get(i));
            }

        } else {
            boolean isCached = false;

            for (int i = 0; i < teamsInController.size(); i++) {
                Team t = teamsInController.get(i);
                if (t.getHackathon().getTitle().equals(title) &&
                        t.getHackathon().getLocation().equals(location))
                    isCached = true;
            }

            if (isCached) {

                System.out.println("DEBUG - Carico da CACHE: " + title + ", refresh=" + refreshing);

                teamNames.clear();
                scores.clear();

                for (int i = 0; i < teamsInController.size(); i++) {
                    if(teamsInController.get(i).getHackathon().getTitle().equals(title)){
                        teamNames.add(teamsInController.get(i).getName());
                        scores.add(scoresInController.get(i));
                    }
                }

            } else {

                teamNames.clear();
                scores.clear();
                HackathonDAO hackathonDAO = new HackathonImplementationDAO();
                hackathonDAO.scoreboard(title, location, teamNames, scores);
                for (int i = 0; i < teamNames.size(); i++) {
                    Team t = new Team(teamNames.get(i), null,
                            new Hackathon(title, location, 0, null, null, null, null, 0, 0, null));
                    teamsInController.add(t);
                    scoresInController.add(scores.get(i));

                    System.out.println("DEBUG - Aggiungo alla cache" + teamsInController.get(i).getName());

                }
            }
        }

        System.out.println("VISUALIZZAZIONE CACHE ---------- \n");
        for(Team t: teamsInController) {
            System.out.println(t.getName());
        }

    }

    public void controllerGetAvailableHackathons(ArrayList<String> titles, ArrayList<String> locations, ArrayList<Integer> periodsOfTime,
                                                 ArrayList<Date> startDates, ArrayList<Date> endDates, ArrayList<Date> startSubDates,
                                                 ArrayList<Date> endSubDates, ArrayList<Integer> maxPlayers, ArrayList<Integer> maxTeamDim,
                                                 boolean refreshing) throws SQLException {
        if (availableHackathons == null || refreshing) {
            UserDAO user = new UserImplementationDAO();
            user.getHackathons(titles, locations, periodsOfTime, startDates, endDates, startSubDates, endSubDates, maxPlayers, maxTeamDim);
            availableHackathons = new ArrayList<Hackathon>();
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

    public Hackathon getAvailableHackathon(String title, String location) {
        for (Hackathon h : availableHackathons) {
            if (h.getTitle().equals(title) && h.getLocation().equals(location)) {return h;}
        }
        return null;
    }
}
