package dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaccia Data Access Object per la gestione delle operazioni relative ai team.
 * Definisce i metodi per pubblicare i progressi dei team attraverso documenti
 * e per recuperare i documenti pubblicati con i relativi commenti dei giudici.
 */
public interface TeamDAO {
    /**
     * Pubblica un documento di progresso per un team nel relativo hackathon.
     * Registra nel database un nuovo documento che rappresenta l'avanzamento
     * del lavoro del team durante l'hackathon specificato.
     *
     * @param teamName  il nome del team che pubblica il documento
     * @param hackTitle il titolo dell'hackathon
     * @param location  la sede dell'hackathon
     * @param docTitle  il titolo del documento da pubblicare
     * @param content   il contenuto del documento
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    void publishProgress(String teamName, String hackTitle, String location, String docTitle, String content) throws SQLException;

    /**
     * Recupera tutti i documenti pubblicati da un team in un dato hackathon.
     * Popola le liste fornite con i titoli, i contenuti e i commenti dei giudici
     * relativi a tutti i documenti di progresso pubblicati dal team per l'hackathon specificato.
     *
     * @param teamName  il nome del team
     * @param hackTitle il titolo dell'hackathon
     * @param location  la sede dell'hackathon
     * @param docTitles lista da popolare con i titoli dei documenti
     * @param contents  lista da popolare con i contenuti dei documenti
     * @param comments  lista da popolare con i commenti dei giudici sui documenti
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    void getDocuments(String teamName, String hackTitle, String location,
                      List<String> docTitles, List<String> contents, List<String> comments) throws SQLException;
}