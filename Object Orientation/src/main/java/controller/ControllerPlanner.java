package controller;

import dao.PlannerDAO;
import postgresImplementationDao.PlannerImplementationDAO;
import model.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControllerPlanner {

    private Controller controller;

    public ControllerPlanner(Controller controller) {this.controller = controller;}

    public void controllerGetUsers(String planUser,
                                   ArrayList<String> allUsernames,
                                   ArrayList<String> allNames,
                                   ArrayList<String> allSurnames,
                                   ArrayList<String> allPasswords) throws SQLException {

        PlannerDAO planner = new PlannerImplementationDAO();

        planner.getUsers(planUser, allUsernames, allNames, allSurnames, allPasswords);

    }

    public void controllerGetHackathons(String username, ArrayList<String> titles, ArrayList<String> locations,
                                        ArrayList<Long> periodOftime, ArrayList<String> problemDescriptions,
                                         ArrayList<Date> startDate, ArrayList<Date> endDate,
                                        ArrayList<Date> startSubDate, ArrayList<Date> endSubDate,
                                        ArrayList<Integer> maxPlayers, ArrayList<Integer> maxTeamDim,boolean refreshing) throws SQLException {


            if(controller.getPlanner().getHackathons().isEmpty() || refreshing){
                PlannerDAO planner = new PlannerImplementationDAO();
                planner.getHackathons(username, titles, locations,periodOftime,problemDescriptions,startDate,endDate,startSubDate,endSubDate,maxPlayers,maxTeamDim);
                for(int i=0;i<titles.size();i++){
                    controller.getPlanner().getHackathons().add(new Hackathon(titles.get(i),locations.get(i),periodOftime.get(i),
                            startDate.get(i),endDate.get(i),startSubDate.get(i),
                            endSubDate.get(i),maxPlayers.get(i),maxTeamDim.get(i),null));
                    try{
                        controller.getPlanner().getHackathons().get(i).setProblemDescription(problemDescriptions.get(i));
                    } catch(Exception e){
                        System.out.println(e.getMessage());
                    }
                }
            }else{
                for (Hackathon myHackathon : controller.getPlanner().getHackathons()) {
                    titles.add(myHackathon.getTitle());
                    locations.add(myHackathon.getLocation());
                    periodOftime.add(myHackathon.getPeriodOfTime());
                    problemDescriptions.add(myHackathon.getProblemDescription());
                    startDate.add(myHackathon.getStartDate());
                    endDate.add(myHackathon.getEndDate());
                    startSubDate.add(myHackathon.getStartDate());
                    endSubDate.add(myHackathon.getEndDate());
                    maxPlayers.add(myHackathon.getMaxPlayers());
                    maxTeamDim.add(myHackathon.getMaxTeamDim());
                }
            }
    }

    public void controllerOpenHackathon(String title, String location, Date startDate, Date endDate,
                                        Date startSubDate, Date endSubDate, int maxPlayers, int maxTeamDim,
                                        String planUsername , String judgesUsernames) throws SQLException{

        PlannerDAO planner = new PlannerImplementationDAO();

        planner.openHackathon(title,location,startDate,endDate,startSubDate,endSubDate,maxPlayers,maxTeamDim,planUsername,judgesUsernames);

    }

    public void controllerStartHackathon(String title, String location) throws SQLException {

        PlannerDAO planner = new PlannerImplementationDAO();

        planner.startHackathon(title,location);

    }

    public void controllerEndHackathon(String title, String location) throws  SQLException{

        PlannerDAO planner = new PlannerImplementationDAO();

        planner.endHackathon(title,location);
    }


}
