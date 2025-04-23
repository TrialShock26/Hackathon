package model;
import java.util.ArrayList;
import static java.time.LocalDate.now;

public class Player extends User {
    private ArrayList<Team> myTeams = new ArrayList<Team>();
    private ArrayList<Registration> mySubscriptions = new ArrayList<Registration>();
    //another arraylist for the hackathons; redundant

    public Player(String username, String password, String name, String surname) {
        super(username, password, name, surname);
    }

    public void signUpHackathon(Hackathon h) {
        if (h.getEndSubscriptionDate().isBefore(now())) {
            System.out.println("Tempo scaduto!");
            return;
        }
        for (Registration r : h.getRegisteredPlayers()) {
            if (r.getPlayer() == this) {
                System.out.println("Sei già registrato");
                return;
            }
        }
        Registration r = new Registration(this, h);
        mySubscriptions.add(r);
        h.setRegisteredPlayers(r);
    }

    public void createTeam(String nomeTeam, Hackathon h) { //setter for myTeams field
        Team t = getThis(nomeTeam);//strenghten the constraint to players&names
        if (t == null) {
            t = new Team (nomeTeam, this);
            myTeams.add(t);
        }
        if (h.getTeams().contains(t)) {
            System.out.println("Team già formato");
        } else {
            h.setTeam(t);
            t.setHackathonsDone(h);
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
    public ArrayList<Registration> getSubscriptions() {return mySubscriptions;}
}
