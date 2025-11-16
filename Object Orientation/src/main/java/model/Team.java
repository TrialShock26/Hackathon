package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta un gruppo di {@link Player} che partecipa a un {@link Hackathon}.
 * <p>
 * Ogni team ha un nome, un numero di membri, una lista di giocatori,
 * una lista dei progressi pubblicati (sotto forma di {@link Document})
 * e una lista di valutazioni ({@link Grade}) ricevute alla fine dell’hackathon.
 */
public class Team {
    private String name;
    private int membersNumber;
    private ArrayList<Player> playerList;
    private Hackathon hackathonMember;
    private ArrayList<Document> progressList;
    private ArrayList<Grade> grades;

    /**
     * Crea un nuovo {@code Team} con un nome specifico, un giocatore iniziale
     * e l'{@link Hackathon} di appartenenza.
     * <p>
     * Il team viene inizializzato con un solo membro e liste vuote
     * per i progressi e le valutazioni.
     *
     * @param teamName il nome del team
     * @param p        il primo giocatore del team
     * @param hack     l'hackathon a cui il team partecipa
     */
    public Team(String teamName, Player p, Hackathon hack) {
        name = teamName;
        membersNumber = 1;
        hackathonMember = hack;
        if (p != null) {
            playerList = new ArrayList<>();
            playerList.add(p);
            p.setTeam(this);
        }
        progressList = new ArrayList<>();
        grades = new ArrayList<>();
    }

    /**
     * Pubblica un nuovo {@link Document} che rappresenta un aggiornamento
     * o un avanzamento del lavoro del team nell’hackathon.
     *
     * @param title   il titolo del documento
     * @param content il contenuto del documento
     */
    public void publishProgress(String title, String content) {
        Document d = new Document(title, content, this);
        progressList.add(d);
    }

    /**
     * Restituisce il nome del team.
     *
     * @return il nome del team
     */
    public String getName() {return name;}

    /**
     * Restituisce il numero di membri attuali del team.
     *
     * @return il numero di membri
     */
    public int getMembersNumber() {return membersNumber;}

    /**
     * Restituisce l'{@link Hackathon} a cui il team è iscritto.
     *
     * @return l'hackathon associato
     */
    public Hackathon getHackathon() {return hackathonMember;}

    /**
     * Restituisce la lista dei {@link Player} che compongono il team.
     *
     * @return la lista dei giocatori
     */
    public List<Player> getPlayers() {return playerList;}

    /**
     * Aggiunge un nuovo {@link Player} al team e aggiorna il numero dei membri.
     *
     * @param p il giocatore da aggiungere
     */
    public void setPlayer(Player p) {
        playerList.add(p);
        membersNumber++;
    }

    /**
     * Rimuove un {@link Player} dal team e aggiorna il numero dei membri.
     *
     * @param p il giocatore che lascia il team
     */
    public void playerLeaving(Player p) {
        playerList.remove(p);
        membersNumber--;
    }

    /**
     * Restituisce la lista dei {@link Document} che rappresentano i progressi pubblicati dal team.
     *
     * @return la lista dei progressi
     */
    public List<Document> getProgress() {return progressList;}

    /**
     * Restituisce la lista delle {@link Grade} ricevute dal team.
     *
     * @return la lista delle valutazioni
     */
    public List<Grade> getGrades() {return grades;}

    /**
     * Aggiunge una nuova {@link Grade} alla lista delle valutazioni del team.
     *
     * @param g la valutazione da aggiungere
     */
    public void setGrade(Grade g) {grades.add(g);}
}