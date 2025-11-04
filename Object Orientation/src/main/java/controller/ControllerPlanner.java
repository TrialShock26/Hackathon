package controller; //TODO javadoc

import dao.PlannerDAO;
import postgresImplementationDao.PlannerImplementationDAO;
import model.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Controller planner.
 */
public class ControllerPlanner {
    /**
     * The My hackathons.
     */
    ArrayList<Hackathon> myHackathons;

    /**
     * Controller get users.
     *
     * @param planUser     the plan user
     * @param allUsernames the all usernames
     * @param allNames     the all names
     * @param allSurnames  the all surnames
     * @param allPasswords the all passwords
     */
    public void controllerGetUsers(String planUser,
                                   List<String> allUsernames,
                                   List<String> allNames,
                                   List<String> allSurnames,
                                   List<String> allPasswords){

        try {
            PlannerDAO planner = new PlannerImplementationDAO();
            planner.getUsers(planUser, allUsernames, allNames, allSurnames, allPasswords);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Controller get hackathons.
     *
     * @param username            the username
     * @param titles              the titles
     * @param locations           the locations
     * @param periodOftime        the period oftime
     * @param problemDescriptions the problem descriptions
     * @param startDate           the start date
     * @param endDate             the end date
     * @param startSubDate        the start sub date
     * @param endSubDate          the end sub date
     * @param maxPlayers          the max players
     * @param maxTeamDim          the max team dim
     * @param refreshing          the refreshing
     * @throws SQLException the sql exception
     */
    public void controllerGetHackathons(String username, List<String> titles, List<String> locations,
                                        List<Long> periodOftime, List<String> problemDescriptions,
                                         List<Date> startDate, List<Date> endDate,
                                        List<Date> startSubDate, List<Date> endSubDate,
                                        List<Integer> maxPlayers, List<Integer> maxTeamDim,boolean refreshing) throws SQLException {

            if(myHackathons==null || refreshing){
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

    /**
     * Controller open hackathon.
     *
     * @param title           the title
     * @param location        the location
     * @param startDate       the start date
     * @param endDate         the end date
     * @param startSubDate    the start sub date
     * @param endSubDate      the end sub date
     * @param maxPlayers      the max players
     * @param maxTeamDim      the max team dim
     * @param planUsername    the plan username
     * @param judgesUsernames the judges usernames
     * @throws SQLException the sql exception
     */
    public void controllerOpenHackathon(String title, String location, Date startDate, Date endDate,
                                        Date startSubDate, Date endSubDate, int maxPlayers, int maxTeamDim,
                                        String planUsername , String judgesUsernames) throws SQLException{

        PlannerDAO planner = new PlannerImplementationDAO();

        planner.openHackathon(title,location,startDate,endDate,startSubDate,endSubDate,maxPlayers,maxTeamDim,planUsername,judgesUsernames);

    }

    /**
     * Controller start hackathon.
     *
     * @param title    the title
     * @param location the location
     * @throws SQLException the sql exception
     */
    public void controllerStartHackathon(String title, String location) throws SQLException {

        PlannerDAO planner = new PlannerImplementationDAO();

        planner.startHackathon(title,location);

    }

    /**
     * Controller end hackathon.
     *
     * @param title    the title
     * @param location the location
     * @throws SQLException the sql exception
     */
    public void controllerEndHackathon(String title, String location) throws  SQLException{

        PlannerDAO planner = new PlannerImplementationDAO();

        planner.endHackathon(title,location);
    }


    /**
     * Gets my hackathons.
     *
     * @return the my hackathons
     */
    public List<Hackathon> getMyHackathons() {
        return myHackathons;
    }
}
