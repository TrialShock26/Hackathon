package model;

import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

/**
 * Rappresenta un partecipante a un hackathon.
 * Un partecipante può iscriversi a uno o più hackathon, unirsi a un team
 * e partecipare alle attività dell'evento.
 * <p>
 * Ogni {@code Player} mantiene l’elenco dei team di cui fa parte e delle iscrizioni effettuate.
 */
public class Player extends User {
    private ArrayList<Team> myTeams;
    private ArrayList<Registration> mySubscriptions;

    /**
     * Costruisce un nuovo oggetto {@code Player} con le informazioni di base da Utente
     * e allocando le varie liste per i dati in cache.
     *
     * @param username il nome utente del partecipante
     * @param password la password del partecipante
     * @param name     il nome del partecipante
     * @param surname  il cognome del partecipante
     */
    public Player(String username, String password, String name, String surname) {
        super(username, password, name, surname);
        myTeams = new ArrayList<>();
        mySubscriptions = new ArrayList<>();
    }

    /**
     * Permette al partecipante di iscriversi a un hackathon, se le condizioni lo consentono.
     * <p>
     * Restituisce un codice numerico che indica l’esito dell’iscrizione:
     * <ul>
     *     <li>0 → Iscrizione completata con successo;</li>
     *     <li>1 → Iscrizioni non ancora aperte o già chiuse;</li>
     *     <li>2 → Il partecipante è già registrato all’hackathon;</li>
     *     <li>3 → Numero massimo di partecipanti raggiunto.</li>
     * </ul>
     *
     * In caso di iscrizione valida, viene creata automaticamente una nuova {@code Registration}
     * e un nuovo {@code Team} associato al partecipante con un nome di default.
     *
     * @param h l'hackathon a cui iscriversi
     * @return un codice numerico che indica l’esito dell’operazione
     */
    public int signUpHackathon(Hackathon h) {
        if (h.getStartSubscriptionDate().after(new Date(System.currentTimeMillis()))) {
            return 1;
        }
        if (h.getEndSubscriptionDate().before(new Date(System.currentTimeMillis()))) {
            return 1;
        }
        for (Registration r : h.getRegisteredPlayers()) {
            if (r.getPlayer() == this) {
                return 2;
            }
        }
        if (h.getRegisteredPlayers().size()+1 > h.getMaxPlayers()) {
            return 3;
        }
        Registration r = new Registration(this, h);
        mySubscriptions.add(r);
        h.setRegisteredPlayer(r);

        Team newTeam = new Team("Team di " + this.getUsername(), this, h);
        myTeams.add(newTeam);
        h.setTeam(newTeam);
        return 0;
    }

    /**
     * Permette al partecipante di unirsi a un team esistente per un determinato hackathon.
     * <p>
     * Restituisce un codice numerico che rappresenta l’esito dell’operazione:
     * <ul>
     *     <li>0 → Operazione riuscita, il partecipante è entrato nel team;</li>
     *     <li>1 → L’hackathon è già iniziato, non è possibile modificare i team;</li>
     *     <li>2 → Il partecipante è già membro del team indicato;</li>
     *     <li>3 → Il team ha raggiunto il numero massimo di membri consentito;</li>
     *     <li>4 → Il partecipante non appartiene ad alcun team iscritto all’hackathon.</li>
     * </ul>
     *
     * @param t il team a cui unirsi
     * @param h l’hackathon di riferimento
     * @return un codice numerico che indica l’esito dell’operazione
     */
    public int joinTeam(Team t, Hackathon h) {
        if (h.getStartDate().before(new Date(System.currentTimeMillis()))) {
            return 1;
        }
        if (myTeams.contains(t)) {
            return 2;
        }
        if (t.getPlayers().size()+1 > h.getMaxTeamDim()) {
            return 3;
        }
        Team old = null;
        for (Team temp : h.getTeams()) {
            if (temp.getPlayers().contains(this)) {
                old = temp;
            }
        }
        if (old == null) {return 4;}
        old.playerLeaving(this);
        if (old.getMembersNumber() == 0) {h.teamLeaving(old);}
        t.setPlayer(this);
        myTeams.add(t);
        return 0;
    }

    /**
     * Restituisce la lista dei team a cui il partecipante appartiene.
     *
     * @return una lista di oggetti {@code Team}
     */
    public List<Team> getTeams() {return myTeams;}

    /**
     * Restituisce la lista delle iscrizioni del partecipante agli hackathon.
     *
     * @return una lista di oggetti {@code Registration}
     */
    public List<Registration> getSubscriptions() {return mySubscriptions;}
}
