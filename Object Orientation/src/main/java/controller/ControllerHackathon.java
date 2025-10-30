package controller;

import dao.HackathonDAO;
import dao.UserDAO;
import model.*;
import postgresImplementationDao.HackathonImplementationDAO;
import postgresImplementationDao.UserImplementationDAO;
import java.sql.Date;
import java.sql.SQLException;
import java.util.*;

public class ControllerHackathon {

    private ArrayList<Hackathon> myHackathons;
    private ArrayList<Team> myScoreboard;
    private ArrayList<Double> myScores;

    public void controllerOverallRanking(ArrayList<String> teamNames, ArrayList<Double> scores,
                                         ArrayList<String> titles, ArrayList<String> locations, boolean refreshing) throws SQLException {

            if(myScoreboard == null || refreshing) {

                myScoreboard = new ArrayList<>();
                myScores = new ArrayList<>();
                HackathonDAO hackathon = new HackathonImplementationDAO();
                hackathon.overallRanking(teamNames,scores,titles,locations);

                for(int i = 0; i < teamNames.size(); i++){
                    myScoreboard.add(new Team(teamNames.get(i),null,null));
                    myScores.add(scores.get(i));
                }

            } else {
                for (int i = 0; i < teamNames.size(); i++) {
                    teamNames.add(myScoreboard.get(i).getName());
                    scores.add(myScores.get(i));
                }
            }
    }

    public void controllerScoreboard(String title, String location,
                                     ArrayList<String> teamNames, ArrayList<Double> scores, boolean refreshing) throws SQLException{

        if(myScoreboard == null || refreshing){

            myScoreboard = new ArrayList<>();
            myScores = new ArrayList<>();
            HackathonDAO hackathon = new HackathonImplementationDAO();
            hackathon.scoreboard(title, location, teamNames, scores);

            for(int i = 0; i < teamNames.size(); i++){
                myScoreboard.add(new Team(teamNames.get(i),null,null));
                myScores.add(scores.get(i));
            }
        } else {
            for(int i=0;i<teamNames.size();i++){
                teamNames.add(myScoreboard.get(i).getName());
                scores.add(myScores.get(i));
            }
        }
    }


    public void controllerGetAvailableHackathonsDB(ArrayList<String> titles, ArrayList<String> locations, ArrayList<Integer> periodsOfTime,
                                                   ArrayList<Date> startDates, ArrayList<Date> endDates, ArrayList<Date> startSubDates,
                                                   ArrayList<Date> endSubDates, ArrayList<Integer> maxPlayers, ArrayList<Integer> maxTeamDim) throws SQLException {
        UserDAO user = new UserImplementationDAO();
        user.getHackathons(titles, locations, periodsOfTime, startDates, endDates, startSubDates, endSubDates, maxPlayers, maxTeamDim);
        myHackathons = new ArrayList<Hackathon>();
        for (int i = 0; i < titles.size(); i++) {
            myHackathons.add(new Hackathon(titles.get(i), locations.get(i), periodsOfTime.get(i).longValue(), startDates.get(i),
                    endDates.get(i), startSubDates.get(i), endSubDates.get(i), maxPlayers.get(i), maxTeamDim.get(i), null));
        } //TODO Planner corretto dal DB
    }

    public void controllerGetClosedHackathons(ArrayList<String> titles, ArrayList<String> locations, boolean refreshing) throws SQLException{

        if(myHackathons==null || refreshing){

            myHackathons = new ArrayList<>();
            HackathonDAO hackathon = new HackathonImplementationDAO();
            hackathon.getClosedHackathons(titles,locations);
            for(int i = 0; i < titles.size(); i++){
                myHackathons.add(new Hackathon(titles.get(i),locations.get(i),
                        0,null,null,null,null,0,0,null));
            }
        }else{
            for(Hackathon hackathon : myHackathons){
                titles.add(hackathon.getTitle());
                locations.add(hackathon.getLocation());
            }
        }

    }
}
