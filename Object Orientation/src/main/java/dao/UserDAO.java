package dao;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Interfaccia Data Access Object per la gestione delle operazioni comuni a tutti gli utenti.
 * Definisce i metodi per l'autenticazione, la registrazione di nuovi utenti
 * e la visualizzazione degli hackathon disponibili nel sistema.
 */
public interface UserDAO {
    /**
     * Esegue il login di un utente nel sistema.
     * Verifica le credenziali fornite e, in caso di successo,
     * restituisce una stringa contenente il suo nome e cognome, formattata come segue:
     * {@code nome@cognome}.
     *
     * @param username il nome utente
     * @param password la password dell'utente
     * @return il nome e cognome dell'utente se il login ha successo, altrimenti null
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    String login(String username, String password) throws SQLException;

    /**
     * Registra un nuovo utente nel sistema.
     * Crea un nuovo account utente nel database con le informazioni fornite.
     *
     * @param username il nome utente desiderato
     * @param password la password dell'utente
     * @param name     il nome dell'utente
     * @param surname  il cognome dell'utente
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    void newUser(String username, String password, String name, String surname)  throws SQLException;

    /**
     * Recupera la lista di tutti gli hackathon disponibili nel sistema a cui è possibile iscriversi.
     * Popola le liste fornite con tutte le informazioni relative agli hackathon
     * presenti nel database, inclusi titoli, sedi, date e parametri di partecipazione.
     *
     * @param titles        lista da popolare con i titoli degli hackathon
     * @param locations     lista da popolare con le sedi degli hackathon
     * @param periodsOfTime lista da popolare con le durate in giorni degli hackathon
     * @param startDates    lista da popolare con le date di inizio degli hackathon
     * @param endDates      lista da popolare con le date di fine degli hackathon
     * @param startSubDates lista da popolare con le date di inizio iscrizioni
     * @param endSubDates   lista da popolare con le date di fine iscrizioni
     * @param maxPlayers    lista da popolare con i numeri massimi di partecipanti
     * @param maxTeamDim    lista da popolare con le dimensioni massime dei team
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    void getHackathons(ArrayList<String> titles, ArrayList<String> locations, ArrayList<Integer> periodsOfTime,
                       ArrayList<Date> startDates, ArrayList<Date> endDates, ArrayList<Date> startSubDates, ArrayList<Date> endSubDates,
                       ArrayList<Integer> maxPlayers, ArrayList<Integer> maxTeamDim) throws SQLException;
}