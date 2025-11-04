package dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaccia Data Access Object per la gestione delle operazioni relative ai giocatori.
 * Definisce i metodi per l'iscrizione agli hackathon, l'adesione ai team,
 * e il recupero di informazioni su hackathon, team e compagni di squadra.
 */
public interface PlayerDAO {
    /**
     * Iscrive un giocatore a un hackathon specifico.
     * Registra nel database la partecipazione del giocatore all'hackathon
     * identificato da titolo e sede.
     *
     * @param username il nome utente del giocatore da iscrivere
     * @param title    il titolo dell'hackathon
     * @param location la sede dell'hackathon
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    void subscribe(String username, String title, String location) throws SQLException;

    /**
     * Aggiunge un giocatore a un team esistente.
     * Registra nel database l'adesione del giocatore al team specificato
     * per un determinato hackathon.
     * La procedura sul database si occupa di gestire lo spostamento del giocatore correttamente.
     *
     * @param username il nome utente del giocatore che si unisce al team
     * @param teamName il nome del team a cui unirsi
     * @param title    il titolo dell'hackathon
     * @param location la sede dell'hackathon
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    void joinTeam(String username, String teamName, String title, String location)  throws SQLException;

    /**
     * Recupera la lista degli hackathon a cui è iscritto un giocatore.
     * Popola le liste fornite con i titoli, le sedi degli hackathon
     * e i nomi dei team a cui appartiene il giocatore per ciascun hackathon.
     *
     * @param username  il nome utente del giocatore
     * @param titles    lista da popolare con i titoli degli hackathon
     * @param locations lista da popolare con le sedi degli hackathon
     * @param teamNames lista da popolare con i nomi dei team del giocatore
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    void getHackathons(String username, List<String> titles, List<String> locations, List<String> teamNames) throws SQLException;

    /**
     * Recupera la lista degli altri team partecipanti a un hackathon.
     * Popola la lista fornita con i nomi di tutti i team dell'hackathon
     * escludendo il team a cui appartiene il giocatore specificato.
     *
     * @param username  il nome utente del giocatore
     * @param title     il titolo dell'hackathon
     * @param location  la sede dell'hackathon
     * @param teamNames lista da popolare con i nomi degli altri team
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    void getOtherTeams(String username, String title, String location, List<String> teamNames) throws SQLException;

    /**
     * Recupera la lista dei compagni di squadra di un giocatore.
     * Popola le liste fornite con nomi e cognomi dei membri del team
     * a cui appartiene il giocatore, escludendo il giocatore stesso.
     *
     * @param username il nome utente del giocatore
     * @param teamName il nome del team
     * @param title    il titolo dell'hackathon
     * @param location la sede dell'hackathon
     * @param names    lista da popolare con i nomi dei compagni di squadra
     * @param surnames lista da popolare con i cognomi dei compagni di squadra
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    void getTeammates(String username, String teamName, String title, String location, List<String> names, List<String> surnames) throws SQLException;
}