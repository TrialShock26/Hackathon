package dao;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Interfaccia Data Access Object per la gestione delle operazioni relative agli hackathon.
 * Definisce i metodi per recuperare classifiche generali e specifiche,
 * e per ottenere informazioni sugli hackathon conclusi.
 */
public interface HackathonDAO {
    /**
     * Recupera la classifica generale di tutti gli hackathon.
     * Popola le liste fornite con i nomi dei team, i loro punteggi,
     * e i titoli e sedi degli hackathon a cui hanno partecipato.
     *
     * @param teamNames lista da popolare con i nomi dei team
     * @param scores    lista da popolare con i punteggi ottenuti dai team
     * @param titles    lista da popolare con i titoli degli hackathon
     * @param locations lista da popolare con le sedi degli hackathon
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    void overallRanking(ArrayList<String> teamNames, ArrayList<Double> scores, ArrayList<String> titles, ArrayList<String> locations) throws SQLException;

    /**
     * Recupera la classifica dei team per uno specifico hackathon.
     * Popola le liste fornite con i nomi dei team e i loro punteggi
     * relativi all'hackathon identificato da titolo e sede.
     *
     * @param title     il titolo dell'hackathon
     * @param location  la sede dell'hackathon
     * @param teamNames lista da popolare con i nomi dei team partecipanti
     * @param scores    lista da popolare con i punteggi dei team
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    void scoreboard(String title, String location, ArrayList<String> teamNames, ArrayList<Double> scores) throws SQLException;

    /**
     * Recupera la lista degli hackathon conclusi.
     * Popola le liste fornite con i titoli e le sedi degli hackathon
     * che sono terminati.
     *
     * @param titles    lista da popolare con i titoli degli hackathon conclusi
     * @param locations lista da popolare con le sedi degli hackathon conclusi
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    void getClosedHackathons(ArrayList<String> titles, ArrayList<String> locations) throws SQLException;
}