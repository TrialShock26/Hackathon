package dao;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Interfaccia Data Access Object per la gestione delle operazioni relative ai giudici.
 * Definisce i metodi per pubblicare problemi, esaminare documenti dei team,
 * assegnare valutazioni e recuperare informazioni su hackathon e team assegnati al giudice.
 */
public interface JudgeDAO {
    /**
     * Pubblica la descrizione del problema per uno specifico hackathon.
     * Salva nel database il testo del problema associato all'hackathon
     * identificato da titolo e sede.
     *
     * @param text     il testo della descrizione del problema da pubblicare
     * @param title    il titolo dell'hackathon
     * @param location la sede dell'hackathon
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    void publishProblem(String text, String title, String location) throws SQLException;

    /**
     * Esamina un documento di un team e registra un commento del giudice.
     * Salva nel database il commento del giudice relativo a un documento specifico
     * di un team partecipante a un hackathon.
     *
     * @param username  il nome utente del giudice che esamina il documento
     * @param docTitle  il titolo del documento da esaminare
     * @param content   il contenuto del documento
     * @param teamName  il nome del team proprietario del documento
     * @param hackTitle il titolo dell'hackathon
     * @param location  la sede dell'hackathon
     * @param text      il commento del giudice sul documento
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    void examineDocument(String username, String docTitle, String content,
                         String teamName, String hackTitle, String location, String text) throws SQLException;

    /**
     * Assegna una valutazione a un team per uno specifico hackathon.
     * Registra nel database il punteggio assegnato dal giudice a un team
     * partecipante a un hackathon.
     *
     * @param username il nome utente del giudice che assegna la valutazione
     * @param teamName il nome del team da valutare
     * @param title    il titolo dell'hackathon
     * @param location la sede dell'hackathon
     * @param value    il punteggio da assegnare al team
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    void gradeTeam(String username, String teamName, String title, String location, int value) throws SQLException;

    /**
     * Recupera la lista degli hackathon a cui è assegnato un giudice.
     * Popola le liste fornite con i titoli, le sedi e le descrizioni
     * dei problemi degli hackathon assegnati al giudice specificato.
     *
     * @param username            il nome utente del giudice
     * @param titles              lista da popolare con i titoli degli hackathon
     * @param locations           lista da popolare con le sedi degli hackathon
     * @param problemDescriptions lista da popolare con le descrizioni dei problemi
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    void getHackathons(String username, ArrayList<String> titles, ArrayList<String> locations,
                       ArrayList<String> problemDescriptions) throws SQLException;

    /**
     * Recupera la lista dei team partecipanti a uno specifico hackathon.
     * Popola la lista fornita con i nomi dei team iscritti all'hackathon
     * identificato da titolo e sede.
     *
     * @param title     il titolo dell'hackathon
     * @param location  la sede dell'hackathon
     * @param teamNames lista da popolare con i nomi dei team partecipanti
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    void getTeams(String title, String location, ArrayList<String> teamNames) throws SQLException;
}