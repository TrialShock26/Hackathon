package controller;

import dao.PlannerDAO;
import postgresImplementationDao.PlannerImplementationDAO;

import java.sql.Date;
import java.util.ArrayList;

public class ControllerPlanner {

    public void controllerGetHackathons(String username, ArrayList<String> titles, ArrayList<String> locations) {
        try {
            PlannerDAO planner = new PlannerImplementationDAO();
            planner.getHackathons(username, titles, locations);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void controllerOpenHackathon(String title, String location, Date startDate, Date endDate,
                                        Date startSubDate, Date endSubDate, int maxPlayers, int maxTeamDim,
                                        String planUsername , String judgesUsernames) {
        try {
            PlannerDAO planner = new PlannerImplementationDAO();

            planner.openHackathon(title,location,startDate,endDate,startSubDate,endSubDate,maxPlayers,maxTeamDim,planUsername,judgesUsernames);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
