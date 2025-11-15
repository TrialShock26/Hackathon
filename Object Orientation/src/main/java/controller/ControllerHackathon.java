package controller;

import dao.HackathonDAO;
import dao.UserDAO;
import postgresImplementationDao.HackathonImplementationDAO;
import postgresImplementationDao.UserImplementationDAO;
import model.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.*;

/**
 * Controller che si occupa di coordinare le operazioni relative agli hackathon,
 * inclusa la gestione delle classifiche, il recupero degli hackathon disponibili
 * e chiusi, e la gestione dei punteggi dei team.
 */
public class ControllerHackathon {
    private ArrayList<Hackathon> availableHackathons;
    private ArrayList<Hackathon> closedHackathons;
    private ArrayList<ArrayList<Double>> scoresMatrix;
    private ArrayList<Team> myOverall;
    private ArrayList<Double> myOverallScores;

    /**
     * Gestisce la visualizzazione della classifica di un hackathon specifico.
     * Recupera i punteggi dei team per un determinato hackathon identificato
     * da titolo e sede. Se i dati non sono già in cache, vengono recuperati
     * dal database. Utilizza una matrice di {@code ArrayList<Double>} per creare
     * una corrispondenza biunivoca tra un hackathon e i suoi punteggi da mostrare.
     *
     * @param title il titolo dell'hackathon
     * @param location la sede dell'hackathon
     * @param teamNames la lista dei nomi dei team
     * @param scores la lista dei punteggi corrispondenti
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public void controllerScoreboard(String title, String location,
                                     List<String> teamNames, List<Double> scores) throws SQLException {
        if (scoresMatrix == null) {
            scoresMatrix = new ArrayList<>();
            for (int i = 0; i < closedHackathons.size(); i++) {
                scoresMatrix.add(new ArrayList<>());
            }
        }
        Hackathon hack = null;
        int idx = -1;
        for (Hackathon h : closedHackathons) {
            if (h.getTitle().equals(title) && h.getLocation().equals(location)) {
                idx = closedHackathons.indexOf(h);
                hack = h;
            }
        }

        if (scoresMatrix.get(idx).isEmpty()) {
            HackathonDAO hackathonDAO = new HackathonImplementationDAO();
            hackathonDAO.scoreboard(title, location, teamNames, scores);

            for (int i = 0; i < teamNames.size(); i++) {
                Team newTeam = new Team(
                        teamNames.get(i),
                        null,
                        hack
                );
                Objects.requireNonNull(hack).setTeam(newTeam);
                scoresMatrix.get(idx).add(scores.get(i));
            }
        } else {
            for (int i = 0; i < Objects.requireNonNull(hack).getTeams().size(); i++) {
                teamNames.add(hack.getTeams().get(i).getName());
                scores.add(scoresMatrix.get(idx).get(i));
            }
        }
    }

    /**
     * Gestisce la classifica generale di tutti i team nella piattaforma.
     * Recupera tutti i team e i relativi punteggi attraverso
     * i vari hackathon. I dati vengono memorizzati localmente e possono essere aggiornati
     * impostando il parametro {@code refreshing} a true.
     *
     * @param teamNames la lista dei nomi dei team
     * @param scores la lista dei punteggi corrispondenti
     * @param titles la lista dei titoli degli hackathon
     * @param locations la lista delle sedi degli hackathon
     * @param refreshing se true, forza l'aggiornamento dei dati dal database
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public void controllerOverallRanking(List<String> teamNames, List<Double> scores,
                                         List<String> titles, List<String> locations,
                                         boolean refreshing) throws SQLException {
        if (myOverall == null || refreshing) {
            myOverall = new ArrayList<>();
            myOverallScores = new ArrayList<>();

            HackathonDAO hackathonDAO = new HackathonImplementationDAO();
            hackathonDAO.overallRanking(teamNames,scores,titles,locations);

            for (int i = 0; i < teamNames.size(); i++) {
                Team newTeam = new Team(
                        teamNames.get(i),
                        null,
                        new Hackathon(titles.get(i), locations.get(i))
                );
                myOverall.add(newTeam);
                myOverallScores.add(scores.get(i));
            }
        } else {
            for (int i = 0; i < myOverall.size(); i++) {
                teamNames.add(myOverall.get(i).getName());
                scores.add(myOverallScores.get(i));
                titles.add(myOverall.get(i).getHackathon().getTitle());
                locations.add(myOverall.get(i).getHackathon().getLocation());
            }
        }
    }

    /**
     * Recupera la lista degli hackathon disponibili per la registrazione.
     * Ottiene tutti gli hackathon a cui è possibile iscriversi, con tutte
     * le relative informazioni (date, numero massimo di giocatori, ecc.).
     * I dati vengono memorizzati localmente e possono essere aggiornati impostando il
     * parametro refreshing a true.
     *
     * @param titles la lista dei titoli degli hackathon
     * @param locations la lista delle sedi degli hackathon
     * @param periodsOfTime la lista delle durate degli hackathon in giorni
     * @param startDates la lista delle date di inizio
     * @param endDates la lista delle date di fine
     * @param startSubDates la lista delle date di inizio iscrizioni
     * @param endSubDates la lista delle date di fine iscrizioni
     * @param maxPlayers la lista dei numeri massimi di giocatori
     * @param maxTeamDim la lista delle dimensioni massime dei team
     * @param refreshing se true, forza l'aggiornamento dei dati dal database
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public void controllerGetAvailableHackathons(List<String> titles, List<String> locations, List<Integer> periodsOfTime,
                                                 List<Date> startDates, List<Date> endDates, List<Date> startSubDates,
                                                 List<Date> endSubDates, List<Integer> maxPlayers, List<Integer> maxTeamDim,
                                                 boolean refreshing) throws SQLException {
        if (availableHackathons == null || refreshing) {
            UserDAO user = new UserImplementationDAO();
            user.getHackathons(titles, locations, periodsOfTime, startDates, endDates, startSubDates, endSubDates, maxPlayers, maxTeamDim);
            availableHackathons = new ArrayList<>();
            for (int i = 0; i < titles.size(); i++) {
                availableHackathons.add(new Hackathon(titles.get(i), locations.get(i), periodsOfTime.get(i).longValue(), startDates.get(i),
                        endDates.get(i), startSubDates.get(i), endSubDates.get(i), maxPlayers.get(i), maxTeamDim.get(i), null));
            }
        } else {
            for (Hackathon availableHackathon : availableHackathons) {
                titles.add(availableHackathon.getTitle());
                locations.add(availableHackathon.getLocation());
                periodsOfTime.add((int)availableHackathon.getPeriodOfTime());
                startDates.add(availableHackathon.getStartDate());
                endDates.add(availableHackathon.getEndDate());
                startSubDates.add(availableHackathon.getStartSubscriptionDate());
                endSubDates.add(availableHackathon.getEndSubscriptionDate());
                maxPlayers.add(availableHackathon.getMaxPlayers());
                maxTeamDim.add(availableHackathon.getMaxTeamDim());
            }
        }
    }

    /**
     * Recupera la lista degli hackathon conclusi.
     * Ottiene tutti gli hackathon che sono terminati e per cui sono
     * disponibili le classifiche finali. I dati vengono memorizzati localmente e possono
     * essere aggiornati impostando il parametro refreshing a true.
     *
     * @param titles la lista dei titoli degli hackathon chiusi
     * @param locations la lista delle sedi degli hackathon chiusi
     * @param refreshing se true, forza l'aggiornamento dei dati dal database
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public void controllerGetClosedHackathons(List<String> titles, List<String> locations, boolean refreshing) throws SQLException{

        if (closedHackathons == null || refreshing) {
            closedHackathons = new ArrayList<>();
            HackathonDAO hackathon = new HackathonImplementationDAO();
            hackathon.getClosedHackathons(titles, locations);
            for(int i = 0; i < titles.size(); i++){
                closedHackathons.add(new Hackathon(titles.get(i),locations.get(i)));
            }
            scoresMatrix = null;
        } else {
            for (Hackathon hackathon : closedHackathons){
                titles.add(hackathon.getTitle());
                locations.add(hackathon.getLocation());
            }
        }
    }

    /**
     * Cerca e restituisce un hackathon disponibile specifico.
     * Ricerca nella lista degli hackathon disponibili quello che corrisponde
     * al titolo e alla sede specificati.
     *
     * @param title il titolo dell'hackathon da cercare
     * @param location la sede dell'hackathon da cercare
     * @return l'hackathon trovato, oppure null se non esiste
     */
    public Hackathon getAvailableHackathon(String title, String location) {
        for (Hackathon h : availableHackathons) {
            if (h.getTitle().equals(title) && h.getLocation().equals(location)) {return h;}
        }
        return null;
    }
}