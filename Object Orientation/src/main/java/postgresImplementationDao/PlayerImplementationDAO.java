package postgresImplementationDao;

import dao.PlayerDAO;
import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;

/**
 * Implementazione dell'interfaccia {@link PlayerDAO} per la gestione dei dati
 * relativi ai partecipanti nel database PostgreSQL.
 */
public class PlayerImplementationDAO implements PlayerDAO {
    private Connection connection;

    /**
     * Costruisce un nuovo oggetto {@code PlayerImplementationDAO} e
     * inizializza la connessione al database.
     */
    public PlayerImplementationDAO() {
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
     * @param username il nome utente del giocatore da iscrivere
     * @param title    il titolo dell'hackathon
     * @param location la sede dell'hackathon
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    @Override
    public void subscribe(String username, String title, String location) throws SQLException {
        CallableStatement cs;
        String query = "DO $$ " +
                "DECLARE " +
                    "hackId Hackathon.id_hackathon%TYPE;" +
                "BEGIN " +
                    "SELECT id_hackathon INTO hackId " +
                    "FROM Hackathon " +
                    "WHERE titolo = '" + title + "' AND sede = '" + location + "';" +
                    "CALL subscribe(hackId, '" + username + "');" +
                "END $$;";
        cs = connection.prepareCall(query);
        cs.execute();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Viene adoperato un blocco anonimo per estrarre gli {@code id} in base ai dati e chiamare
     * correttamente le procedure sul database, preservando l'indipendenza del codice dal
     * tipo di implementazione scelta per la base di dati.
     *
     * @param username il nome utente del giocatore che si unisce al team
     * @param teamName il nome del team a cui unirsi
     * @param title    il titolo dell'hackathon
     * @param location la sede dell'hackathon
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    @Override
    public void joinTeam(String username, String teamName, String title, String location) throws SQLException {
        CallableStatement cs;
        String query = "DO $$ " +
                "DECLARE " +
                    "teamId Team.id_team%TYPE;" +
                "BEGIN " +
                    "SELECT id_team INTO teamId " +
                    "FROM Team NATURAL JOIN Hackathon " +
                    "WHERE nome = '" + teamName + "' AND titolo = '" + title + "' AND sede = '" + location + "';" +
                    "CALL join_team('" + username + "', teamId);" +
                "END $$;";
        cs = connection.prepareCall(query);
        cs.execute();
    }

    @Override
    public void getHackathons(String username, ArrayList<String> titles, ArrayList<String> locations, ArrayList<String> teamNames) throws SQLException {
        PreparedStatement ps;
        String query = "SELECT titolo, sede, nome " +
                       "FROM Hackathon NATURAL JOIN Team NATURAL JOIN Partecipazione " +
                       "WHERE id_partecipante = (SELECT id_partecipante " +
                                                "FROM Partecipante " +
                                                "WHERE username = ?);";
        ps = connection.prepareStatement(query);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            titles.add(rs.getString("titolo"));
            locations.add(rs.getString("sede"));
            teamNames.add(rs.getString("nome"));
        }
        rs.close();
    }

    @Override
    public void getOtherTeams(String username, String title, String location, ArrayList<String> teamNames) throws SQLException {
        PreparedStatement ps;
        String query = "SELECT nome " +
                "FROM Team NATURAL JOIN Hackathon " +
                "WHERE titolo = ? AND sede = ? " +
                  "AND nome NOT IN (SELECT t.nome " +
                                   "FROM Team t NATURAL JOIN Partecipazione NATURAL JOIN Partecipante p " +
                                        "JOIN Utente u ON u.username = p.username " +
                                   "WHERE u.username = ?)";
        ps = connection.prepareStatement(query);
        ps.setString(1, title);
        ps.setString(2, location);
        ps.setString(3, username);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            teamNames.add(rs.getString("nome"));
        }
        rs.close();
    }

    @Override
    public void getTeammates(String username, String teamName, String title, String location, ArrayList<String> names, ArrayList<String> surnames) throws SQLException {
        PreparedStatement ps;
        String query = "SELECT u.nome, cognome " +
                "FROM Partecipante p NATURAL JOIN Partecipazione NATURAL JOIN Team t NATURAL JOIN Hackathon " +
                    "JOIN Utente u ON u.username = p.username " +
                "WHERE t.nome = ? AND titolo = ? AND sede = ? AND u.username <> ?;";
        ps = connection.prepareStatement(query);
        ps.setString(1, teamName);
        ps.setString(2, title);
        ps.setString(3, location);
        ps.setString(4, username);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            names.add(rs.getString("nome"));
            surnames.add(rs.getString("cognome"));
        }
        rs.close();
    }
}