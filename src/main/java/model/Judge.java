package model;
import java.util.ArrayList;

public class Judge extends User {
    private Hackathon myHackathon;
    private ArrayList<Team> judgingTeams= new ArrayList<Team>();

    public Judge(String username, String password, String name, String surname, Hackathon h) {
        super(username, password, name, surname);
        myHackathon = h;
    }

    public void publishProblem (Hackathon h, String problem) {
        h.setProblemDescription(problem);
    }

    public void commentDocument (Document d, String comment) {
        d.setComment(comment);
    }

    public ArrayList<Team> getTeams() {return judgingTeams;}
    public void setTeam(Team t) {
        judgingTeams.add(t);
    }

    public Hackathon getHackathon() {return myHackathon;}
}