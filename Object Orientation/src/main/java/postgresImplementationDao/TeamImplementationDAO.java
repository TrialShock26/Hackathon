package postgresImplementationDao;

import dao.TeamDAO;
import database.DatabaseConnection;
import java.sql.*;
import java.util.List;

/**
 * Implementazione dell'interfaccia {@link TeamDAO} per la gestione dei dati
 * relativi ai giudici nel database PostgreSQL.
 */
public class TeamImplementationDAO implements TeamDAO {
    private Connection connection;

    /**
     * Costruisce un nuovo oggetto {@code TeamImplementationDAO} e
     * inizializza la connessione al database.
     */
    public TeamImplementationDAO() {
        try {
            connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Viene adoperato un blocco anonimo per estrarre gli {@code id} in base ai dati e chiamare
     * correttamente le procedure sul database, preservando l'indipendenza del codice dal
     * tipo di implementazione scelta per la base di dati.
     *
     * @param teamName  il nome del team che pubblica il documento
     * @param hackTitle il titolo dell'hackathon
     * @param location  la sede dell'hackathon
     * @param docTitle  il titolo del documento da pubblicare
     * @param content   il contenuto del documento
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    @Override
    public void publishProgress(String teamName, String hackTitle, String location, String docTitle, String content) throws SQLException {
        CallableStatement cs;
        String query = "DO $$ " +
                "DECLARE teamId Team.id_team%TYPE;" +
                    "BEGIN " +
                        "SELECT id_team INTO teamId " +
                        "FROM Team NATURAL JOIN Hackathon " +
                        "WHERE nome = '"+ teamName +"' AND titolo = '"+ hackTitle +"' AND sede = '"+ location +"';" +
                    "CALL publish_progress(teamId, '"+ content +"', '"+ docTitle +"');" +
                "END $$;";
        cs = connection.prepareCall(query);
        cs.execute();
    }

    @Override
    public void getDocuments(String teamName, String hackTitle, String location,
                             List<String> docTitles, List<String> contents, List<String> comments) throws SQLException {
        PreparedStatement ps;
        String query = "SELECT d.titolo, d.contenuto, d.commento " +
                "FROM Documento d NATURAL JOIN Team t JOIN Hackathon h ON t.id_hackathon = h.id_hackathon " +
                "WHERE t.nome = ? AND h.titolo = ? AND h.sede = ?; ";
        ps = connection.prepareStatement(query);
        ps.setString(1, teamName);
        ps.setString(2, hackTitle);
        ps.setString(3, location);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            docTitles.add(rs.getString("titolo"));
            contents.add(rs.getString("contenuto"));
            comments.add(rs.getString("commento"));
        }
        rs.close();
    }
}