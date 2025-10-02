package model;
import java.sql.Date;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Hackathon {
    private String title;
    private String location;
    private Date startDate;
    private Date endDate;
    private long periodOfTime;
    private Date startSubscriptionDate;
    private Date endSubscriptionDate;
    private int maxPlayers;
    private int maxTeamDim;
    private String problemDescription;
    private ArrayList<Registration> registeredPlayers;
    private ArrayList<Team> teams;
    private ArrayList<Judge> judges;
    private Planner planner;

    public Hackathon(String title, String location, Date startDate, Date endDate, long periodOfTime,
                     Date startSubscriptionDate, Date endSubscriptionDate, int maxPlayers, int maxTeamDim, Planner planner) {
        this.title = title;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.periodOfTime = periodOfTime;
        this.startSubscriptionDate = startSubscriptionDate;
        this.endSubscriptionDate = endSubscriptionDate;
        this.maxPlayers = maxPlayers;
        this.maxTeamDim = maxTeamDim;
        problemDescription = null;
        registeredPlayers = new ArrayList<Registration>();
        teams = new ArrayList<Team>();
        judges = new ArrayList<Judge>();
        this.planner = planner;
    }

    public String getTitle () {return title;}
    public void setTitle (String title) {this.title = title;}

    public String getLocation () {return location;}
    public void setLocation (String location) {this.location = location;}

    public Date getStartDate () {return startDate;}
    /*public void setStartDate (Date startDate) {
        this.startDate = startDate;
        periodOfTime = ChronoUnit.DAYS.between(startDate, endDate);
    }*/

    public Date getEndDate () {return endDate;}
    /*public void setEndDate (Date endDate) {
        this.endDate = endDate;
        periodOfTime = ChronoUnit.DAYS.between(startDate, endDate);
    }*/

    public long getPeriodOfTime () {return periodOfTime;}

    public Date getStartSubscriptionDate () {return startSubscriptionDate;}
    /*public void setStartSubscriptionDate (Date startSubscriptionDate) {
        this.startSubscriptionDate = startSubscriptionDate;
        registrationWindow = ChronoUnit.DAYS.between(startSubscriptionDate, endSubscriptionDate);
    }*/

    public Date getEndSubscriptionDate () {return endSubscriptionDate;}
    /*public void setEndSubscriptionDate (Date endSubscriptionDate) {
        this.endSubscriptionDate = endSubscriptionDate;
        registrationWindow = ChronoUnit.DAYS.between(startSubscriptionDate, endSubscriptionDate);
    }*/

    //public long getRegistrationWindow () {return registrationWindow;}

    public int getMaxPlayers () {return maxPlayers;}
    public void setMaxPlayers (int maxPlayers) {this.maxPlayers = maxPlayers;}

    public int getMaxTeamDim () {return maxTeamDim;}
    public void setMaxTeamDim (int maxTeamDim) {this.maxTeamDim = maxTeamDim;}

    public String getProblemDescription () {
        if (problemDescription == null) {
            System.out.println("Ancora assente...");
            return null;
        } else {return problemDescription;}
    }
    public void setProblemDescription(String text) {this.problemDescription = text;}

    public ArrayList<Registration> getRegisteredPlayers() {return registeredPlayers;}
    public void setRegisteredPlayers(Registration r) {registeredPlayers.add(r);}

    public ArrayList<Team> getTeams() {return teams;}
    public void setTeam(Team t) {teams.add(t);}

    public ArrayList<Judge> getJudges() {return judges;}
    public void setJudge(Judge j) {judges.add(j);}

    public Planner getPlanner () {return planner;}
    public void setPlanner (Planner p) {this.planner = p;}
}
