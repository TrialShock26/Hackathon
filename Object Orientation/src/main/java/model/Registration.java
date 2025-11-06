package model;

import java.sql.Date;

/**
 * Rappresenta la registrazione di un {@link Player} a un determinato {@link Hackathon}.
 * <p>
 * Ogni registrazione include il giocatore, l'hackathon a cui partecipa
 * e la data in cui è avvenuta la registrazione.
 */
public class Registration {
    private Player player;
    private Hackathon hackathon;
    private Date registrationDate;

    /**
     * Crea una nuova istanza di {@code Registration} associando un {@link Player}
     * e un {@link Hackathon}, impostando automaticamente la data di registrazione
     * alla data e ora correnti del sistema.
     *
     * @param player    il giocatore che si registra
     * @param hackathon l'hackathon a cui il giocatore partecipa
     */
    public Registration (Player player, Hackathon hackathon) {
        this.player = player;
        this.hackathon = hackathon;
        this.registrationDate = new Date(System.currentTimeMillis());
    }

    /**
     * Restituisce il {@link Player} associato a questa registrazione.
     *
     * @return il giocatore registrato
     */
    public Player getPlayer() {return player;}

    /**
     * Restituisce l'{@link Hackathon} a cui il giocatore si è registrato.
     *
     * @return l'hackathon associato
     */
    public Hackathon getHackathon() {return hackathon;}

    /**
     * Restituisce la data in cui è avvenuta la registrazione.
     *
     * @return la data della registrazione
     */
    public Date getRegistrationDate() {return registrationDate;}
}
