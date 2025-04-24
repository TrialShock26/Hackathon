package model;
import java.time.LocalDate;
import java.util.ArrayList;

public class Planner extends User {
    private ArrayList<Hackathon> myHackathons;

    public Planner(String username, String password, String nome, String cognome) {
        super(username, password, nome, cognome);
        myHackathons = new ArrayList<Hackathon>();
    }

    public void inviteJudge (Hackathon h, String username, String password, String name, String surname) {
        Judge g = new Judge(username, password, name, surname, h);
        h.setJudge(g);
    }

    public void openHackathon (String title, String location, LocalDate startDate, LocalDate endDate,
                                    LocalDate startSubscriptionDate, LocalDate endSubscriptionDate,
                                    int maxPlayers, int maxTeamDim) {
        Hackathon h = new Hackathon(title, location, startDate, endDate, startSubscriptionDate, endSubscriptionDate, maxPlayers, maxTeamDim, this);
        myHackathons.add(h);
    }

    public ArrayList<Hackathon> getHackathons() {return myHackathons;}
}