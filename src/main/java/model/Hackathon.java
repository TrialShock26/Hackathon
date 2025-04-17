package model;
import java.util.Date;

public class Hackathon {
    private String titolo;
    private String sede;
    private Date dataInizio;
    private Date dataFine;
    private int maxDimTeam;
    private int maxIscritti;

    public Hackathon(String titolo, String sede, Date dataInizio, Date dataFine, int maxIscritti,
                     int maxDimTeam) {
        this.titolo = titolo;
        this.sede = sede;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.maxIscritti = maxIscritti;
        this.maxDimTeam = maxDimTeam;
    }
}
