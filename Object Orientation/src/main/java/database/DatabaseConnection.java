package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe per la gestione della connessione al database PostgreSQL.
 * Implementa il pattern Singleton per garantire una singola istanza della connessione
 * al database condivisa nell'intera applicazione, ottimizzando le risorse e
 * gestendo automaticamente la riconnessione in caso di chiusura della connessione.
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    /**
     * Costruttore privato per implementare il pattern Singleton.
     * Inizializza la connessione al database PostgreSQL utilizzando
     * i parametri di configurazione specificati (URL, utente e password).
     * In caso di errore durante la connessione, stampa il messaggio di errore.
     */
    private DatabaseConnection() {
        try {
            String url = "jdbc:postgresql://ep-restless-rain-a2a23td0-pooler.eu-central-1.aws.neon.tech/neondb?user=neondb_owner&password=npg_uRIqoh0V6lnb&sslmode=require&channelBinding=require";
            String user = "neondb_owner";
            String password = "npg_uRIqoh0V6lnb";
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }

    /**
     * Restituisce l'istanza unica della connessione al database.
     * Se l'istanza non esiste o la connessione è stata chiusa,
     * ne crea una nuova. Implementa il pattern Singleton con controllo
     * dello stato della connessione.
     *
     * @return l'istanza unica di DatabaseConnection
     * @throws SQLException se si verifica un errore nel verificare lo stato della connessione
     */
    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null)
            instance = new DatabaseConnection();
        else if (instance.connection.isClosed())
            instance = new DatabaseConnection();
        return instance;
    }

    /**
     * Restituisce l'oggetto Connection per interagire con il database.
     * Questo metodo permette di ottenere la connessione attiva per
     * eseguire query e operazioni sul database.
     *
     * @return l'oggetto Connection al database PostgreSQL
     */
    public Connection getConnection() {
        return connection;
    }
}