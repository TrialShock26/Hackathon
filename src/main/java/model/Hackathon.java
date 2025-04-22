package model;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Hackathon {
    private String title;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private long periodOfTime;
    private LocalDate startSubscriptionDate;
    private LocalDate endSubscriptionDate;//may be derivable
    private long registrationWindow;
    private int maxPlayers;
    private int maxTeamDim;
    private String problemDescription;
    private ArrayList<Registration> registeredPlayers = new ArrayList<Registration>();
    private ArrayList<Team> teams = new ArrayList<Team>();
    private ArrayList<Judge> judges = new ArrayList<Judge>();
    private Planner planner;

    public Hackathon(String title, String location, LocalDate startDate, LocalDate endDate,
                     LocalDate startSubscriptionDate, LocalDate endSubscriptionDate, int maxPlayers, int maxTeamDim, Planner planner) {
        this.title = title;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        periodOfTime = ChronoUnit.DAYS.between(startDate, endDate);
        this.startSubscriptionDate = startSubscriptionDate;
        this.endSubscriptionDate = endSubscriptionDate;
        registrationWindow = ChronoUnit.DAYS.between(startSubscriptionDate, endSubscriptionDate);
        this.maxPlayers = maxPlayers;
        this.maxTeamDim = maxTeamDim;
        this.planner = planner;
        problemDescription = null;
    }

    public String getTitle () {return title;}
    public void setTitle (String title) {this.title = title;}

    public String getLocation () {return location;}
    public void setLocation (String location) {this.location = location;}

    public LocalDate getStartDate () {return startDate;}
    public void setStartDate (LocalDate startDate) {
        this.startDate = startDate;
        periodOfTime = ChronoUnit.DAYS.between(startDate, endDate);
    }

    public LocalDate getEndDate () {return endDate;}
    public void setEndDate (LocalDate endDate) {
        this.endDate = endDate;
        periodOfTime = ChronoUnit.DAYS.between(startDate, endDate);
    }

    public long getPeriodOfTime () {return periodOfTime;}

    public LocalDate getStartSubscriptionDate () {return startSubscriptionDate;}
    public void setStartSubscriptionDate (LocalDate startSubscriptionDate) {
        this.startSubscriptionDate = startSubscriptionDate;
        registrationWindow = ChronoUnit.DAYS.between(startSubscriptionDate, endSubscriptionDate);
    }

    public LocalDate getEndSubscriptionDate () {return endSubscriptionDate;}
    public void setEndSubscriptionDate (LocalDate endSubscriptionDate) {
        this.endSubscriptionDate = endSubscriptionDate;
        registrationWindow = ChronoUnit.DAYS.between(startSubscriptionDate, endSubscriptionDate);
    }

    public long getRegistrationWindow () {return registrationWindow;}

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
