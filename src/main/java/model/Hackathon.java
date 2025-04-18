package model;
import java.time.LocalDate;
import java.util.ArrayList;

public class Hackathon {
    private String title;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private int maxPlayers;
    private int maxTeamDim;
    private String problemDescription;
    private ArrayList<Player> players = new ArrayList<Player>();
    private ArrayList<Team> teams = new ArrayList<Team>();
    private ArrayList<Judge> judges = new ArrayList<Judge>();

    public Hackathon(String title, String location, LocalDate startDate, LocalDate endDate, int maxPlayers, int maxTeamDim) {
        this.title = title;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxPlayers = maxPlayers;
        this.maxTeamDim = maxTeamDim;
    }

    public String getTitle () {return title;}
    public void setTitle (String title) {this.title = title;}

    public String getLocation () {return location;}
    public void setLocation (String location) {this.location = location;}

    public LocalDate getStartDate () {return startDate;}
    public void setStartDate (LocalDate startDate) {this.startDate = startDate;}

    public LocalDate getEndDate () {return endDate;}
    public void setEndDate (LocalDate endDate) {this.endDate = endDate;}

    public int getMaxPlayers () {return maxPlayers;}
    public void setMaxPlayers (int maxPlayers) {this.maxPlayers = maxPlayers;}

    public int getMaxTeamDim () {return maxTeamDim;}
    public void setMaxTeamDim (int maxTeamDim) {this.maxTeamDim = maxTeamDim;}

    public String getProblemDescription () {return problemDescription;}
    public void setProblemDescription(String text) {this.problemDescription = text;}

    public ArrayList<Player> getPlayers () {return players;}
    public void setPlayer (Player p) {players.add(p);}

    public ArrayList<Team> getTeams() {return teams;}
    public void setTeam(Team t) {teams.add(t);}

    public ArrayList<Judge> getJudges() {return judges;}
    public void setJudge(Judge j) {judges.add(j);}
}
