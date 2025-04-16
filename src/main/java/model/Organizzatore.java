package model;

public class Organizzatore extends Utente {
    public Organizzatore() {}

    private inviteJudge (Giudice g) {
    
    }

    private openRegistrations () {

    }

    private createHackathon (String titolo, String sede, int durata, int maxIscritti, int maxDimTeam) {
        Hackathon h = new Hackathon(titolo, sede, durata, maxDimTeam,maxIscritti);
    }
}