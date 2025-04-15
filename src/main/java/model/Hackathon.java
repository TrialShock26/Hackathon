package model;

public class Hackathon {
    private String titolo;
    private String sede;
    private int durata;
    private int maxDimTeam;
    private int maxIscritti;

    public Hackathon(String titolo, String sede, int durata, int maxIscritti, int maxDimTeam) {
        this.titolo = titolo;
        this.sede = sede;
        this.durata = durata;
        this.maxIscritti = maxIscritti;
        this.maxDimTeam = maxDimTeam;
    }
}
