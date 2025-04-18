package model;
import java.time.LocalDate;

public class Planner extends User {
    public Planner(String username, String password, String nome, String cognome) {
        super(username, password, nome, cognome);
    }

    public void inviteJudge (Hackathon h, String username, String password, String name, String surname) {
        Judge g = new Judge(username, password, name, surname, h);
        h.setJudge(g);
    }

    public Hackathon openHackathon (String title, String location, LocalDate startDate, LocalDate endDate, int maxPlayers, int maxTeamDim) {
        Hackathon h = new Hackathon(title, location, startDate, endDate, maxPlayers, maxTeamDim);
        return h;
    }
}