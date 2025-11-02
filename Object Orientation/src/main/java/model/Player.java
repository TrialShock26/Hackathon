package model;

import java.util.ArrayList;
import java.sql.Date;

public class Player extends User {
    private ArrayList<Team> myTeams;
    private ArrayList<Registration> mySubscriptions;

    public Player(String username, String password, String name, String surname) {
        super(username, password, name, surname);
        myTeams = new ArrayList<>();
        mySubscriptions = new ArrayList<>();
    }

    public int signUpHackathon(Hackathon h) {
        if (h.getStartSubscriptionDate().after(new Date(System.currentTimeMillis()))) {
            return 1;
        }
        if (h.getEndSubscriptionDate().before(new Date(System.currentTimeMillis()))) {
            return 1;
        }
        for (Registration r : h.getRegisteredPlayers()) {
            if (r.getPlayer() == this) {
                return 2;
            }
        }
        if (h.getRegisteredPlayers().size()+1 > h.getMaxPlayers()) {
            return 3;
        }
        Registration r = new Registration(this, h);
        mySubscriptions.add(r);
        h.setRegisteredPlayers(r);

        Team newTeam = new Team("Team di " + this.getUsername(), this, h);
        myTeams.add(newTeam);
        h.setTeam(newTeam);
        return 0;
    }

    public int joinTeam(Team t, Hackathon h) {
        if (h.getStartDate().before(new Date(System.currentTimeMillis()))) {
            return 1;
        }
        if (myTeams.contains(t)) {
            return 2;
        }
        if (t.getPlayers().size()+1 > h.getMaxTeamDim()) {
            return 3;
        }
        Team old = null;
        for (Team temp : h.getTeams()) {
            if (temp.getPlayers().contains(this)) {
                old = temp;
            }
        }
        if (old == null) {return 4;}
        old.playerLeaving(this);
        if (old.getMembersNumber() == 0) {h.teamLeaving(old);}
        t.setPlayer(this);
        myTeams.add(t);
        return 0;
    }

    public ArrayList<Team> getTeams() {return myTeams;}
    public ArrayList<Registration> getSubscriptions() {return mySubscriptions;}
}
