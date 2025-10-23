package controller;

import dao.PlannerDAO;
import model.Hackathon;
import model.Planner;
import postgresImplementationDao.PlannerImplementationDAO;

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
                                         ArrayList<Date> startDate, ArrayList<Date> endDate,
                                        ArrayList<Date> startSubDate, ArrayList<Date> endSubDate,
                                        ArrayList<Integer> maxPlayers, ArrayList<Integer> maxTeamDim) throws SQLException {

            if(myHackathons==null){
                myHackathons = new ArrayList<>();
                PlannerDAO planner = new PlannerImplementationDAO();
                planner.getHackathons(username, titles, locations,startDate,endDate,startSubDate,endSubDate,maxPlayers,maxTeamDim);
                for(int i=0;i<titles.size();i++){
                    myHackathons.add(new Hackathon(titles.get(i),locations.get(i),0,startDate.get(i),endDate.get(i),startSubDate.get(i),
                            endSubDate.get(i),maxPlayers.get(i),maxTeamDim.get(i),null));
                }
            }else{
                for(int i=0;i<myHackathons.size();i++){
                    titles.add(myHackathons.get(i).getTitle());
                    locations.add(myHackathons.get(i).getLocation());
                    startDate.add(myHackathons.get(i).getStartDate());
                    endDate.add(myHackathons.get(i).getEndDate());
                    startSubDate.add(myHackathons.get(i).getStartDate());
                    endSubDate.add(myHackathons.get(i).getEndDate());
                    maxPlayers.add(myHackathons.get(i).getMaxPlayers());
                    maxTeamDim.add(myHackathons.get(i).getMaxTeamDim());
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

    public ArrayList<Hackathon> getMyHackathons() {
        return myHackathons;
    }
}
