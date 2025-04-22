package model;

public class Registration {
    private Player player;
    private Hackathon hackathon;

    public Registration (Player player, Hackathon hackathon) {
        this.player = player;
        this.hackathon = hackathon;
    }

    public Player getPlayer() {return player;}
    public Hackathon getHackathon() {return hackathon;}
}
