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
    private Planner planner;
    private Selection selection;

    public Hackathon(String title, String location, long periodOfTime, Date startDate, Date endDate,
                     Date startSubscriptionDate, Date endSubscriptionDate, int maxPlayers, int maxTeamDim,
                     Planner planner) {
        this.title = title;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.periodOfTime = periodOfTime;
        this.startSubscriptionDate = startSubscriptionDate;
        this.endSubscriptionDate = endSubscriptionDate;
        this.maxPlayers = maxPlayers;
        this.maxTeamDim = maxTeamDim;
        problemDescription = "Descrizione assente.";
        registeredPlayers = new ArrayList<Registration>();
        teams = new ArrayList<Team>();
        this.planner = planner;
        this.selection = null;
    }

    public String getTitle () {return title;}
    public String getLocation () {return location;}
    public Date getStartDate () {return startDate;}
    public Date getEndDate () {return endDate;}
    public long getPeriodOfTime () {return periodOfTime;}
    public Date getStartSubscriptionDate () {return startSubscriptionDate;}
    public Date getEndSubscriptionDate () {return endSubscriptionDate;}
    public int getMaxPlayers () {return maxPlayers;}
    public int getMaxTeamDim () {return maxTeamDim;}

    public String getProblemDescription () {return problemDescription;}
    public void setProblemDescription(String text) throws IllegalAccessException {
        if (problemDescription != "Descrizione assente.") {
            throw new IllegalAccessException();
        }
        this.problemDescription = text;
    }

    public ArrayList<Registration> getRegisteredPlayers() {return registeredPlayers;}
    public void setRegisteredPlayers(Registration r) {registeredPlayers.add(r);}

    public ArrayList<Team> getTeams() {return teams;}
    public void setTeam(Team t) {teams.add(t);}

    public Planner getPlanner () {return planner;}

    public Selection getSelection() {return selection;}
    public void setSelection(Selection s) {selection = s;}

    public void teamLeaving(Team t) {
        teams.remove(t);
    }
}
