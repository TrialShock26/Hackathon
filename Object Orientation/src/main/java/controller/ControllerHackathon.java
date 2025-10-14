package controller;

import dao.HackathonDAO;
import postgresImplementationDao.HackathonImplementationDAO;
import java.util.ArrayList;

public class ControllerHackathon {

    public void controllerOverallRanking(ArrayList<String> teamNames, ArrayList<Double> scores, ArrayList<String> titles, ArrayList<String> locations) {

        try {
            HackathonDAO hackathon = new HackathonImplementationDAO();

            hackathon.overallRanking(teamNames,scores,titles,locations);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void controllerScoreboard(String title, String location, ArrayList<String> teamNames, ArrayList<Double> scores) {
        try {
            HackathonDAO hackathon = new HackathonImplementationDAO();

            hackathon.scoreboard(title,location,teamNames,scores);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void controllerGetClosedHackathons(ArrayList<String> titles, ArrayList<String> locations){
        try {
            HackathonDAO hackathon = new HackathonImplementationDAO();

            hackathon.getClosedHackathons(titles,locations);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
