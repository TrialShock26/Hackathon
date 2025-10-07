package model;

import java.sql.Date;

public class Registration {
    private Player player;
    private Hackathon hackathon;
    private Date registrationDate;

    public Registration (Player player, Hackathon hackathon) {
        this.player = player;
        this.hackathon = hackathon;
        this.registrationDate = new Date(System.currentTimeMillis());
    }

    public Player getPlayer() {return player;}
    public Hackathon getHackathon() {return hackathon;}
    public Date getRegistrationDate() {return registrationDate;}
}
