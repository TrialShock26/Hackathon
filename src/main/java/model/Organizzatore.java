package model;

public class Organizzatore extends Utente {
    public Organizzatore() {}

    private void inviteJudge (Giudice g) {
    
    }

    private void openRegistrations () {

    }

    private Hackathon createHackathon (String titolo, String sede, int durata, int maxIscritti, int maxDimTeam) {
        Hackathon h = new Hackathon(titolo, sede, durata, maxIscritti, maxDimTeam);
        return h;
    }
}