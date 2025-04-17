package model;
import java.util.Date;

public class Organizzatore extends Utente {
    public Organizzatore(String nome, String cognome) {
        super(nome, cognome);
    }

    private void inviteJudge (Giudice g) {
    
    }

    private Hackathon openHackathon (String titolo, String sede, Date dataInizio, Date dataFine,
                                     int maxIscritti, int maxDimTeam) {
        Hackathon h = new Hackathon(titolo, sede, dataInizio, dataFine, maxIscritti, maxDimTeam);
        return h;
    }
}