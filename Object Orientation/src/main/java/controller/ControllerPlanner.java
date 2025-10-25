package controller;

import dao.PlannerDAO;
import model.Hackathon;
import model.Planner;
import postgresImplementationDao.PlannerImplementationDAO;

import java.sql.Array;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControllerPlanner {

    ArrayList<Hackathon> myHackathons;

    public void controllerGetUsers(String planUser,
                                   ArrayList<String> allUsernames,
                                   ArrayList<String> allNames,
                                   ArrayList<String> allSurnames,
                                   ArrayList<String> allPasswords){

        try {
            PlannerDAO planner = new PlannerImplementationDAO();
            planner.getUsers(planUser, allUsernames, allNames, allSurnames, allPasswords);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void controllerGetHackathons(String username, ArrayList<String> titles, ArrayList<String> locations,
                                        ArrayList<Long> periodOftime, ArrayList<String> problemDescriptions,
                                         ArrayList<Date> startDate, ArrayList<Date> endDate,
                                        ArrayList<Date> startSubDate, ArrayList<Date> endSubDate,
                                        ArrayList<Integer> maxPlayers, ArrayList<Integer> maxTeamDim) throws SQLException {

            if(myHackathons==null){
                myHackathons = new ArrayList<>();
                PlannerDAO planner = new PlannerImplementationDAO();
                planner.getHackathons(username, titles, locations,periodOftime,problemDescriptions,startDate,endDate,startSubDate,endSubDate,maxPlayers,maxTeamDim);
                for(int i=0;i<titles.size();i++){
                    myHackathons.add(new Hackathon(titles.get(i),locations.get(i),periodOftime.get(i),
                            startDate.get(i),endDate.get(i),startSubDate.get(i),
                            endSubDate.get(i),maxPlayers.get(i),maxTeamDim.get(i),null));
                    try{
                        myHackathons.get(i).setProblemDescription(problemDescriptions.get(i));
                    } catch(Exception e){
                        System.out.println(e.getMessage());
                    }
                }
            }else{
                for (Hackathon myHackathon : myHackathons) {
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
                                        String planUsername , String judgesUsernames) {
        try {
            PlannerDAO planner = new PlannerImplementationDAO();

            planner.openHackathon(title,location,startDate,endDate,startSubDate,endSubDate,maxPlayers,maxTeamDim,planUsername,judgesUsernames);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void controllerStartHackathon(String title, String location) throws SQLException {

        PlannerDAO planner = new PlannerImplementationDAO();

        planner.startHackathon(title,location);

    }

    public void controllerEndHackathon(String title, String location) throws  SQLException{

        PlannerDAO planner = new PlannerImplementationDAO();

        planner.endHackathon(title,location);
    }

    public ArrayList<Hackathon> getMyHackathons() {
        return myHackathons;
    }
}
