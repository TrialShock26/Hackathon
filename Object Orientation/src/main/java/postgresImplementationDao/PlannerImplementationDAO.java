package postgresImplementationDao;

import dao.PlannerDAO;
import database.DatabaseConnection;
import java.sql.*;
import java.util.List;

/**
 * Implementazione dell'interfaccia {@link PlannerDAO} per la gestione dei dati
 * relativi agli organizzatori nel database PostgreSQL.
 */
public class PlannerImplementationDAO implements PlannerDAO {
    private Connection connection;

    /**
     * Costruisce un nuovo oggetto {@code PlannerImplementationDAO} e
     * inizializza la connessione al database.
     */
    public PlannerImplementationDAO() {
        try {
            connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void openHackathon(String title, String location, Date startDate, Date endDate,
                              Date startSubDate, Date endSubDate, int maxPlayers, int maxTeamDim,
                              String planUsername, String judgesUsernames) throws SQLException {
        CallableStatement query;
        query = connection.prepareCall("CALL add_hackathon(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        query.setString(1, title);
        query.setString(2, location);
        query.setDate(3, startDate);
        query.setDate(4, endDate);
        query.setDate(5, startSubDate);
        query.setDate(6, endSubDate);
        query.setInt(7, maxPlayers);
        query.setInt(8, maxTeamDim);
        query.setString(9, planUsername);
        query.setString(10, judgesUsernames);
        query.execute();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Viene adoperato un blocco anonimo per estrarre gli {@code id} in base ai dati e chiamare
     * correttamente le procedure sul database, preservando l'indipendenza del codice dal
     * tipo di implementazione scelta per la base di dati. Inoltre, le stringhe in input sono rese
     * sicure per PostgreSQL sostituendo i caratteri non ammessi.
     *
     * @param title    il titolo dell'hackathon da avviare
     * @param location la sede dell'hackathon da avviare
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */

    @Override
    public void startHackathon(String title, String location) throws SQLException {
        CallableStatement cs;

        title = title.replace("'", "''");
        location = location.replace("'", "''");

        String query =
                "DO $$ " +
                        "DECLARE " +
                        "    hackId Hackathon.id_hackathon%TYPE;" +
                        "BEGIN " +
                        "    SELECT id_hackathon INTO hackId " +
                        "    FROM Hackathon " +
                        "    WHERE titolo = '" + title + "' AND sede = '" + location + "'; " +
                        "    IF hackId IS NULL THEN " +
                        "        RAISE EXCEPTION 'Hackathon non trovato: " + title + " - " + location + "'; " +
                        "    END IF; " +
                        "    CALL start_hackathon(hackId); " +
                        "END $$;";

        cs = connection.prepareCall(query);
        cs.execute();
    }

    @Override
    public void endHackathon(String title, String location) throws SQLException {
        connection.setAutoCommit(false);
        CallableStatement cs;
        ResultSet rs;
        String query = "{ ? = CALL end_hackathon((SELECT id_hackathon " +
                                                    "FROM Hackathon " +
                                                    "WHERE titolo = ? AND sede = ?)) }";
        cs = connection.prepareCall(query);
        cs.registerOutParameter(1, Types.OTHER);
        cs.setString(2, title);
        cs.setString(3, location);
        cs.execute();
        rs = (ResultSet) cs.getObject(1);
        rs.close();
        connection.commit();
        connection.setAutoCommit(true);
    }

    @Override
    public void getHackathons(String username, List<String> titles, List<String> locations, List<Long> periodsOfTime,
                              List<String> problemDescriptions, List<Date> startDates, List<Date> endDates,List<Date> startSubDate,List<Date> endSubDate,
                              List<Integer> maxPlayers, List<Integer> maxTeamDim) throws SQLException {
        PreparedStatement ps;
        String query = "SELECT titolo, sede , durata, data_inizio, data_fine, descrizione_problema, " +
                "data_apertura_iscrizioni, data_chiusura_iscrizioni, max_iscritti, max_dim_team " +
                "FROM Hackathon " +
                "WHERE id_organizzatore = (SELECT id_organizzatore " +
                                            "FROM Organizzatore " +
                                            "WHERE username = ?);";
        ps = connection.prepareStatement(query);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            titles.add(rs.getString("titolo"));
            locations.add(rs.getString("sede"));
            periodsOfTime.add(rs.getLong("durata"));
            problemDescriptions.add(rs.getString("descrizione_problema"));
            startDates.add(rs.getDate("data_inizio"));
            endDates.add(rs.getDate("data_fine"));
            startSubDate.add(rs.getDate("data_apertura_iscrizioni"));
            endSubDate.add(rs.getDate("data_chiusura_iscrizioni"));
            maxPlayers.add(rs.getInt("max_iscritti"));
            maxTeamDim.add(rs.getInt("max_dim_team"));
        }
        rs.close();
    }

    @Override
    public void getUsers(String planUser, List<String> allUsernames) throws SQLException {
        PreparedStatement ps;
        String query = "SELECT username " +
                       "FROM Utente " +
                       "WHERE username <> ? AND username <> 'username';";
        ps = connection.prepareStatement(query);
        ps.setString(1, planUser);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            allUsernames.add(rs.getString("username"));
        }
        rs.close();
    }
}
