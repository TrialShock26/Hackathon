package dao;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

/**
 * Interfaccia Data Access Object per la gestione delle operazioni relative agli organizzatori.
 * Definisce i metodi per creare e gestire hackathon, controllare il loro stato
 * (apertura, avvio, chiusura) e recuperare informazioni su hackathon e utenti del sistema disponibili per l'invito come giudici.
 */
public interface PlannerDAO {
    /**
     * Apre un nuovo hackathon con tutti i parametri specificati.
     * Crea un nuovo hackathon nel database con le date di svolgimento,
     * le date di iscrizione, i limiti di partecipanti e i giudici assegnati.
     *
     * @param title           il titolo dell'hackathon
     * @param location        la sede dove si svolge l'hackathon
     * @param startDate       la data di inizio dell'hackathon
     * @param endDate         la data di fine dell'hackathon
     * @param startSubDate    la data di inizio delle iscrizioni
     * @param endSubDate      la data di fine delle iscrizioni
     * @param maxPlayers      il numero massimo di partecipanti totali
     * @param maxTeamDim      la dimensione massima di un team
     * @param planUsername    il nome utente dell'organizzatore che organizza l'hackathon
     * @param judgesUsernames i nomi utente dei giudici assegnati all'hackathon
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    void openHackathon(String title, String location, Date startDate, Date endDate,
                       Date startSubDate, Date endSubDate, int maxPlayers, int maxTeamDim,
                       String planUsername, String judgesUsernames) throws SQLException;

    /**
     * Avvia un hackathon precedentemente aperto.
     * Sfrutta la procedura presente sul database per verificare che esistano le condizioni corrette
     * per l'avvio della competizione.
     *
     * @param title    il titolo dell'hackathon da avviare
     * @param location la sede dell'hackathon da avviare
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    void startHackathon(String title, String location) throws SQLException;

    /**
     * Conclude un hackathon in corso.
     * Sfrutta la procedura presente sul database per effettuare le procedure di conclusione e calcolo della classifica finale.
     *
     * @param title    il titolo dell'hackathon da concludere
     * @param location la sede dell'hackathon da concludere
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    void endHackathon(String title, String location) throws SQLException;

    /**
     * Recupera la lista degli hackathon organizzati da un organizzatore.
     * Popola le liste fornite con tutte le informazioni relative agli hackathon
     * creati dall'organizzatore specificato, inclusi titoli, sedi, date e parametri.
     *
     * @param username            il nome utente dell'organizzatore
     * @param titles              lista da popolare con i titoli degli hackathon
     * @param locations           lista da popolare con le sedi degli hackathon
     * @param periodsOfTime        lista da popolare con la durata in millisecondi degli hackathon
     * @param problemDescriptions lista da popolare con le descrizioni dei problemi
     * @param startDate           lista da popolare con le date di inizio degli hackathon
     * @param endDate             lista da popolare con le date di fine degli hackathon
     * @param startSubDate        lista da popolare con le date di inizio iscrizioni
     * @param endSubDate          lista da popolare con le date di fine iscrizioni
     * @param maxPlayers          lista da popolare con i numeri massimi di partecipanti
     * @param maxTeamDim          lista da popolare con le dimensioni massime dei team
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    void getHackathons(String username, List<String> titles, List<String> locations, List<Long> periodsOfTime,
                       List<String> problemDescriptions,List<Date> startDate, List<Date> endDate,
                       List<Date> startSubDate, List<Date> endSubDate,
                       List<Integer> maxPlayers, List<Integer> maxTeamDim) throws SQLException;

    /**
     * Recupera la lista di tutti gli utenti registrati nel sistema per procedere all'invito come giudice.
     * Popola le liste fornite con gli username di tutti gli utenti,
     * escludendo l'organizzatore che effettua la richiesta.
     *
     * @param planUser     il nome utente dell'organizzatore che richiede i dati
     * @param allUsernames lista da popolare con gli username degli utenti
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    void getUsers(String planUser, List<String> allUsernames) throws SQLException;
}