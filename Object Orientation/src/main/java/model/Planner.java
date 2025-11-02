package model;

import java.sql.Date;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Planner extends User {
    private ArrayList<Hackathon> myHackathons;
    private ArrayList<Selection> mySelections;

    public Planner(String username, String password, String nome, String cognome) {
        super(username, password, nome, cognome);
        myHackathons = new ArrayList<>();
        mySelections = new ArrayList<>();
    }

    public void inviteJudge (Hackathon h, Judge j) {
        if (h.getSelection() == null) {
            h.setSelection(new Selection(this, h));
            h.getSelection().setJudge(j);
        } else {
            h.getSelection().setJudge(j);
        }
    }

    public void openHackathon (String title, String location, Date startDate, Date endDate,
                               Date startSubscriptionDate, Date endSubscriptionDate,
                               int maxPlayers, int maxTeamDim) {
        Hackathon h = new Hackathon(title, location, ChronoUnit.DAYS.between(startDate.toLocalDate(), endDate.toLocalDate())+1,
                startDate, endDate, startSubscriptionDate, endSubscriptionDate, maxPlayers, maxTeamDim, this);
        myHackathons.add(h);
    }

    public ArrayList<Hackathon> getHackathons() {return myHackathons;}
    public ArrayList<Selection> getSelections() {return mySelections;}
}