package controller;

import dao.PlayerDAO;
import postgresImplementationDao.PlayerImplementationDAO;
import model.Hackathon;
import model.Player;
import model.Team;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Controller per la gestione delle operazioni relative ai giocatori negli hackathon.
 * Questa classe gestisce l'interazione tra il giocatore e gli hackathon a cui partecipa,
 * inclusa la gestione dei team, l'iscrizione agli eventi e il recupero delle informazioni
 * sui compagni di squadra e sugli altri team disponibili.
 */
public class ControllerPlayer {
    private Controller controller;
    private ArrayList<Hackathon> myHackathons;

    /**
     * Costruttore della classe ControllerPlayer con relativo riferimento al
     * {@link Controller} padre.
     *
     * @param controller il controller principale dell'applicazione
     */
    public ControllerPlayer(Controller controller) {this.controller = controller;}

    /**
     * Recupera la lista degli hackathon a cui il giocatore partecipa.
     * Se i dati non sono già in cache o è richiesto un aggiornamento, interroga il database
     * per ottenere gli hackathon e i relativi team. Altrimenti utilizza i dati memorizzati localmente.
     *
     * @param username   lo username del giocatore
     * @param titles     la lista da popolare con i titoli degli hackathon
     * @param locations  la lista da popolare con le sedi degli hackathon
     * @param teamNames  la lista da popolare con i nomi dei team del giocatore
     * @param refreshing true se si vuole forzare l'aggiornamento dei dati dal database
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public void controllerGetHackathons(String username, List<String> titles, List<String> locations,
                                        List<String> teamNames,boolean refreshing) throws SQLException{


        if (myHackathons == null || refreshing) {
                myHackathons = new ArrayList<>();
                PlayerDAO player = new PlayerImplementationDAO();
                player.getHackathons(username, titles, locations, teamNames);

                for (int i = 0; i < locations.size(); i++) {
                    Hackathon hack = new Hackathon(titles.get(i), locations.get(i));
                    Team newTeam = new Team(teamNames.get(i),
                            controller.getPlayer(),
                            hack);
                    hack.setTeam(newTeam);
                    myHackathons.add(hack);
                }
            } else {
                for(int i = 0; i < myHackathons.size(); i++){
                    titles.add(myHackathons.get(i).getTitle());
                    locations.add(myHackathons.get(i).getLocation());
                    teamNames.add(controller.getPlayer().getTeams().get(i).getName());
                }
            }
    }

    /**
     * Recupera la lista degli altri team disponibili per un determinato hackathon.
     * Esclude i team a cui il giocatore è già iscritto, mostrando solo quelli
     * a cui è possibile unirsi.
     *
     * @param username  lo username del giocatore
     * @param title     il titolo dell'hackathon
     * @param location  la sede dell'hackathon
     * @param teamNames la lista da popolare con i nomi degli altri team disponibili
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public void controllerGetOtherTeams(String username, String title, String location,
                                        List<String> teamNames) throws SQLException {
            PlayerDAO player = new PlayerImplementationDAO();
            player.getOtherTeams(username,title,location,teamNames);
    }

    /**
     * Permette al giocatore di unirsi a un team esistente per un determinato hackathon.
     * Registra l'associazione tra il giocatore e il team nel database e nella memoria locale.
     *
     * @param username lo username del giocatore che vuole unirsi al team
     * @param teamName il nome del team a cui unirsi
     * @param title    il titolo dell'hackathon
     * @param location la sede dell'hackathon
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public void controllerJoinTeam(String username, String teamName, String title, String location) throws SQLException {
            PlayerDAO player = new PlayerImplementationDAO();
            player.joinTeam(username,teamName,title,location);
            //controller.getPlayer().joinTeam();
    }

    /**
     * Recupera la lista dei compagni di squadra del giocatore in un team specifico.
     * Se i dati non sono in cache o è richiesto un aggiornamento, li recupera dal database.
     * Include automaticamente il giocatore stesso nella lista dei membri del team.
     *
     * @param username   lo username del giocatore
     * @param teamName   il nome del team
     * @param title      il titolo dell'hackathon
     * @param location   la sede dell'hackathon
     * @param names      la lista da popolare con i nomi dei compagni di squadra
     * @param surnames   la lista da popolare con i cognomi dei compagni di squadra
     * @param refreshing true se si vuole forzare l'aggiornamento dei dati dal database
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public void controllerGetTeammates(String username, String teamName, String title, String location,
                                       List<String> names, List<String> surnames, boolean refreshing) throws SQLException {

        Team t = findTeam(teamName, title, location);
        if (Objects.requireNonNull(t).getPlayers().size() == 1 || refreshing) {

            PlayerDAO playerDAO = new PlayerImplementationDAO();
            playerDAO.getTeammates(username, teamName, title, location, names, surnames);
            t.getPlayers().clear();

            t.setPlayer(controller.getPlayer());
            for (int j = 0; j < names.size(); j++) {
                t.setPlayer(new Player(null, null,
                                names.get(j),
                                surnames.get(j))
                );
            }

            names.add((controller.getUser().getName()));
            surnames.add((controller.getUser().getSurname()));
        } else {
            for (int j = 0; j < t.getPlayers().size(); j++) {
                names.add(t.getPlayers().get(j).getName());
                surnames.add(t.getPlayers().get(j).getSurname());
            }
        }
    }

    /**
     * Metodo di supporto per recuperare un team dalla lista dei team a cui è iscritto il giocatore.
     *
     * @param teamName il nome del team
     * @param title    il titolo dell'hackathon
     * @param location la sede dell'hackathon
     * @return il team cercato o {@code null} se non esiste
     */
    private Team findTeam(String teamName, String title, String location) {
        for (int i = 0; i < controller.getPlayer().getTeams().size(); i++) {
            if (controller.getPlayer().getTeams().get(i).getName().equals(teamName) &&
                    controller.getPlayer().getTeams().get(i).getHackathon().getTitle().equals(title) &&
                    controller.getPlayer().getTeams().get(i).getHackathon().getLocation().equals(location)) {
                return controller.getPlayer().getTeams().get(i);
            }
        }
        return null;
    }

    /**
     * Iscrive il giocatore a un hackathon specifico.
     * Registra l'iscrizione nel database e aggiorna la memoria locale
     * aggiungendo l'hackathon alla lista degli eventi a cui partecipa.
     *
     * @param username lo username del giocatore che si vuole iscrivere
     * @param title    il titolo dell'hackathon
     * @param location la sede dell'hackathon
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public void subscribe(String username, String title, String location) throws SQLException {
        PlayerDAO player = new PlayerImplementationDAO();
        player.subscribe(username, title, location);
        controller.getPlayer().signUpHackathon(controller.getControllerHackathon().getAvailableHackathon(title, location));
    }
}