package model;
import java.util.ArrayList;

public class Player extends User {
    private ArrayList<Team> myTeams = new ArrayList<Team>();
    private ArrayList<Hackathon> mySubscriptions = new ArrayList<Hackathon>();//MAYBE

    public Player(String username, String password, String name, String surname) {
        super(username, password, name, surname);
    }

    public void signUpHackathon(Hackathon h) {
        //check availability of registrations
        mySubscriptions.add(h);
        h.setPlayer(this);
    }

    public void createTeam(String nomeTeam, Hackathon h) { //setter for myTeams field
        Team t = getThis(nomeTeam);
        if (t == null) {
            t = new Team (nomeTeam, this);
            myTeams.add(t);
        }
        h.setTeam(t);
        ArrayList<Judge> now = h.getJudges();
        for (Judge j : now) {
            j.setTeam(t);
        }
    }

    public void joinTeam(Team t, Hackathon h) {//setter for myTeams field
        if (t.getPlayers().size() >= h.getMaxPlayers()) {
            System.out.println("Il team è pieno!");
        } else {
            t.setPlayer(this);
            myTeams.add(t);
        }
    }

    public boolean exists(String name) {
        for (Team t : myTeams) {
            if (t.getName().equals(name)) {return true;}
        }
        return false;
    }

    private Team getThis(String name) {
        if (exists(name)) {
            for (Team t : myTeams) {
                if (t.getName().equals(name)) {return t;}
            }
        }
        return null;
    }

    public ArrayList<Team> getTeams() {return myTeams;}
}
