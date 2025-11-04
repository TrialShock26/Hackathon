package model;

import java.sql.Date;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta un organizzatore di hackathon.
 * Un organizzatore ha la possibilità di creare nuovi hackathon, invitare giudici
 * e gestire le selezioni associate agli hackathon organizzati.
 * <p>
 * Ogni organizzatore mantiene una lista dei propri hackathon e delle selezioni gestite.
 */
public class Planner extends User {
    private ArrayList<Hackathon> myHackathons;
    private ArrayList<Selection> mySelections;

    /**
     * Costruisce un nuovo oggetto {@code Planner} con le informazioni di base da Utente
     * e allocando le varie liste per i dati in cache.
     *
     * @param username il nome utente dell'organizzatore
     * @param password la password dell'organizzatore
     * @param nome     il nome dell'organizzatore
     * @param cognome  il cognome dell'organizzatore
     */
    public Planner(String username, String password, String nome, String cognome) {
        super(username, password, nome, cognome);
        myHackathons = new ArrayList<>();
        mySelections = new ArrayList<>();
    }

    /**
     * Invita un giudice a partecipare a un hackathon.
     * <p>
     * Se l'hackathon non ha ancora una selezione associata, il metodo ne crea una nuova
     * e vi assegna il giudice indicato. Se invece la selezione esiste già, il giudice
     * viene semplicemente aggiunto a quella esistente.
     *
     * @param h l'hackathon a cui assegnare il giudice
     * @param j il giudice da invitare
     */
    public void inviteJudge (Hackathon h, Judge j) {
        if (h.getSelection() == null) {
            h.setSelection(new Selection(this, h));
            h.getSelection().setJudge(j);
        } else {
            h.getSelection().setJudge(j);
        }
    }

    /**
     * Crea e apre un nuovo hackathon con i parametri forniti.
     * <p>
     * Il metodo calcola automaticamente la durata dell'hackathon in giorni
     * e aggiunge l'evento alla lista degli hackathon gestiti dall'organizzatore.
     *
     * @param title                 il titolo dell'hackathon
     * @param location              la sede in cui si svolge l'evento
     * @param startDate             la data di inizio dell'hackathon
     * @param endDate               la data di fine dell'hackathon
     * @param startSubscriptionDate la data di apertura delle iscrizioni
     * @param endSubscriptionDate   la data di chiusura delle iscrizioni
     * @param maxPlayers            il numero massimo di partecipanti ammessi
     * @param maxTeamDim            la dimensione massima consentita per ogni team
     */
    public void openHackathon (String title, String location, Date startDate, Date endDate,
                               Date startSubscriptionDate, Date endSubscriptionDate,
                               int maxPlayers, int maxTeamDim) {
        Hackathon h = new Hackathon(title, location, ChronoUnit.DAYS.between(startDate.toLocalDate(), endDate.toLocalDate())+1,
                startDate, endDate, startSubscriptionDate, endSubscriptionDate, maxPlayers, maxTeamDim, this);
        myHackathons.add(h);
    }

    /**
     * Restituisce la lista degli hackathon organizzati dall'organizzatore.
     *
     * @return la lista degli hackathon creati
     */
    public List<Hackathon> getHackathons() {return myHackathons;}

    /**
     * Restituisce la lista delle selezioni gestite dall'organizzatore.
     *
     * @return la lista delle selezioni
     */
    public List<Selection> getSelections() {return mySelections;}
}
