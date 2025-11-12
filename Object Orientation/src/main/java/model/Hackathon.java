package model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta un hackathon organizzato da un organizzatore.
 * Contiene tutte le informazioni relative all'evento, come titolo, luogo, date, iscrizioni,
 * partecipanti, squadre, e la descrizione del problema proposto.
 * <p>
 * Ogni hackathon prevede un periodo di tempo definito, un numero massimo di giocatori e la dimensione
 * massima delle squadre. I partecipanti possono registrarsi entro un periodo di iscrizione prestabilito.
 */
public class Hackathon {
    private String title;
    private String location;
    private Date startDate;
    private Date endDate;
    private long periodOfTime;
    private Date startSubscriptionDate;
    private Date endSubscriptionDate;
    private int maxPlayers;
    private int maxTeamDim;
    private String problemDescription;
    private ArrayList<Registration> registeredPlayers;
    private ArrayList<Team> teams;
    private Planner planner;
    private Selection selection;

    /**
     * Costruisce un nuovo oggetto {@code Hackathon} con i parametri specificati.
     * La descrizione del problema è inizialmente impostata a un valore di default.
     *
     * @param title                 il titolo dell'hackathon
     * @param location              la sede in cui si svolge l'hackathon
     * @param periodOfTime          la durata dell'hackathon in giorni
     * @param startDate             la data di inizio dell'hackathon
     * @param endDate               la data di fine dell'hackathon
     * @param startSubscriptionDate la data di inizio delle iscrizioni
     * @param endSubscriptionDate   la data di chiusura delle iscrizioni
     * @param maxPlayers            il numero massimo di partecipanti ammessi
     * @param maxTeamDim            la dimensione massima di ciascun team
     * @param planner               l'organizzatore dell'evento
     */
    public Hackathon(String title, String location, long periodOfTime, Date startDate, Date endDate,
                     Date startSubscriptionDate, Date endSubscriptionDate, int maxPlayers, int maxTeamDim,
                     Planner planner) {
        this.title = title;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.periodOfTime = periodOfTime;
        this.startSubscriptionDate = startSubscriptionDate;
        this.endSubscriptionDate = endSubscriptionDate;
        this.maxPlayers = maxPlayers;
        this.maxTeamDim = maxTeamDim;
        problemDescription = "Descrizione assente.";
        registeredPlayers = new ArrayList<>();
        teams = new ArrayList<>();
        this.planner = planner;
        this.selection = null;
    }

    /**
     * Costruttore semplificato per ottenere un oggetto {@code Hackathon} con parametri operativi
     * assenti, a eccezione di titolo e sede
     *
     * @param title     il titolo dell'hackathon
     * @param location  la sede in cui si svolge l'hackathon
     */
    public Hackathon(String title, String location) {
        this.title = title;
        this.location = location;
        startDate = null;
        endDate = null;
        periodOfTime = 0;
        startSubscriptionDate = null;
        endSubscriptionDate = null;
        maxPlayers = 0;
        maxTeamDim = 0;
        problemDescription = "Descrizione assente.";
        registeredPlayers = new ArrayList<>();
        teams = new ArrayList<>();
        planner = null;
        selection = null;
    }

    /**
     * Restituisce il titolo dell'hackathon.
     *
     * @return il titolo
     */
    public String getTitle() {return title;}

    /**
     * Restituisce la sede dell'hackathon.
     *
     * @return la sede
     */
    public String getLocation() {return location;}

    /**
     * Restituisce la data di inizio dell'hackathon.
     *
     * @return la data di inizio
     */
    public Date getStartDate() {return startDate;}

    /**
     * Restituisce la data di fine dell'hackathon.
     *
     * @return la data di fine
     */
    public Date getEndDate() {return endDate;}

    /**
     * Restituisce la durata dell'hackathon.
     *
     * @return la durata in giorni
     */
    public long getPeriodOfTime() {return periodOfTime;}

    /**
     * Restituisce la data di apertura delle iscrizioni.
     *
     * @return la data di apertura iscrizioni
     */
    public Date getStartSubscriptionDate() {return startSubscriptionDate;}

    /**
     * Restituisce la data di chiusura delle iscrizioni.
     *
     * @return la data di chiusura iscrizioni
     */
    public Date getEndSubscriptionDate() {return endSubscriptionDate;}

    /**
     * Restituisce il numero massimo di partecipanti ammessi.
     *
     * @return il numero massimo di partecipanti
     */
    public int getMaxPlayers() {return maxPlayers;}

    /**
     * Restituisce la dimensione massima consentita per un team.
     *
     * @return la dimensione massima del team
     */
    public int getMaxTeamDim() {return maxTeamDim;}

    /**
     * Restituisce la descrizione del problema da risolvere durante l'hackathon.
     *
     * @return la descrizione del problema
     */
    public String getProblemDescription() {return problemDescription;}

    /**
     * Imposta la descrizione del problema, se non è già stata definita.
     *
     * @param text la descrizione del problema
     * @throws IllegalAccessException se si tenta di modificare una descrizione già impostata
     */
    public void setProblemDescription(String text) throws IllegalAccessException {
        if (!problemDescription.equals("Descrizione assente.")) {
            throw new IllegalAccessException();
        }
        this.problemDescription = text;
    }

    /**
     * Restituisce la lista delle registrazioni per l'hackathon.
     *
     * @return l'elenco delle registrazioni
     */
    public List<Registration> getRegisteredPlayers() {return registeredPlayers;}

    /**
     * Aggiunge una registrazione di un partecipante all'elenco
     *
     * @param r l'oggetto {@code Registration} da aggiungere
     */
    public void setRegisteredPlayer(Registration r) {registeredPlayers.add(r);}

    /**
     * Restituisce la lista delle squadre partecipanti all'hackathon.
     *
     * @return la lista dei {@code Team}
     */
    public List<Team> getTeams() {return teams;}

    /**
     * Aggiunge una squadra alla lista delle squadre partecipanti.
     *
     * @param t la squadra da aggiungere
     */
    public void setTeam(Team t) {teams.add(t);}

    /**
     * Restituisce l'organizzatore dell'hackathon.
     *
     * @return l'oggetto {@code Planner}
     */
    public Planner getPlanner() {return planner;}

    /**
     * Restituisce la selezione di riferimento per l'hackathon
     *
     * @return la selezione
     */
    public Selection getSelection() {return selection;}

    /**
     * Imposta la selezione relativa all'hackathon.
     *
     * @param s la selezione da impostare
     */
    public void setSelection(Selection s) {selection = s;}

    /**
     * Rimuove una squadra dalla lista delle squadre partecipanti,
     * ad esempio in caso di cambio team.
     *
     * @param t la squadra da rimuovere
     */
    public void teamLeaving(Team t) {
        teams.remove(t);
    }
}
