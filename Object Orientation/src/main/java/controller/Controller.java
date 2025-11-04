package controller;

import dao.*;
import postgresImplementationDao.*;
import model.*;
import java.sql.SQLException;

/**
 * Rappresenta il livello "Controller" dell'architettura BCE + DAO, agisce da container per i vari controller
 * dedicati alle operazioni specifiche.
 * <p>
 * Si occupa di memorizzare le informazioni generali per l'utente loggato e di svolgere le operazioni comuni che non ricadono
 * in un controller specifico.
 */
public class Controller {
    private User user;
    private Player player;
    private Planner planner;
    private Judge judge;
    private ControllerHackathon controllerHackathon;
    private ControllerPlanner controllerPlanner;
    private ControllerPlayer controllerPlayer;
    private ControllerTeam controllerTeam;
    private ControllerJudge controllerJudge;

    /**
     * Instanzia un nuovo Controller "padre" per iniziare a conservare le informazioni temporanee.
     * <p>
     * Tutte le variabili d'istanza sono poste a {@code null} per essere poi gestite correttamente.
     */
    public Controller() {
        user = null;
        player = null;
        planner = null;
        judge = null;
        controllerHackathon = null;
        controllerPlanner = null;
        controllerPlayer = null;
        controllerTeam = null;
    }

    /**
     * Effettua il login alla piattaforma in base alle credenziali fornite.
     * <p>
     * Sfrutta la procedura presente sul database descritta in {@link UserDAO} per autenticare correttamente l'utente.
     * La stringa {@code nameSurname} subisce poi un processo di parsing per completare la creazione dell'oggetto User
     *
     * @param username l'username dell'utente
     * @param password la password dell'utente
     * @return {@code true} se il login ha avuto successo, {@code false} altrimenti.
     */
    public boolean login(String username, String password) {
        String nameSurname;
        UserDAO userDB = new UserImplementationDAO();

        try {
            nameSurname = userDB.login(username, password);
        } catch (SQLException e) {
            return false;
        }

        if (nameSurname != null) {
            int idx = nameSurname.indexOf('@');
            user = new User(username, password, nameSurname.substring(0, idx), nameSurname.substring(idx+1));
            return true;
        }
        return false;
    }

    /**
     * Registra un nuovo utente alla piattaforma in base alle credenziali fornite.
     * <p>
     * Sfrutta la procedura presente sul database descritta in {@link UserDAO} per registrare correttamente l'utente e
     * creare il relativo oggetto User corrispondente.
     *
     * @param username l'username dell'utente
     * @param name     il nome dell'utente
     * @param surname  il cognome dell'utente
     * @param password la password dell'utente
     * @return {@code true} se la registrazione ha avuto successo, {@code false} altrimenti.
     */
    public boolean newUser(String username, String name, String surname, String password) {
        UserDAO userDB = new UserImplementationDAO();

        try {
            userDB.newUser(username, name, surname, password);
            user = new User(username, password, name, surname);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    /**
     * Ritorna l'oggetto User riferito alle credenziali di login.
     *
     * Questo riferimento non può essere mai nullo poiché l'accesso alla piattaforma garantisce l'esistenza di un oggetto.
     *
     * @return l'oggetto User dell'utente loggato.
     */
    public User getUser() {return user;}

    /**
     * Ritorna l'oggetto Player per gestire le operazioni da Partecipante effettuate dall'utente. Se non esiste, viene
     * allocato preventivamente, in quanto ogni utente può ricoprire più ruoli.
     *
     * @return l'oggetto Player dell'utente loggato.
     */
    public Player getPlayer() {
        if (player == null) {
            player = new Player(user.getUsername(), user.getPassword(), user.getName(), user.getSurname());
        }
        return player;
    }

    /**
     * Ritorna l'oggetto Planner per gestire le operazioni da Organizzatore effettuate dall'utente. Se non esiste, viene
     * allocato preventivamente, in quanto ogni utente può ricoprire più ruoli.
     *
     * @return l'oggetto Planner dell'utente loggato.
     */
    public Planner getPlanner() {
        if (planner == null) {
            planner = new Planner(user.getUsername(), user.getPassword(), user.getName(), user.getSurname());
        }
        return planner;
    }

    /**
     * Ritorna l'oggetto Judge per gestire le operazioni da Giudce effettuate dall'utente. Se non esiste, viene
     * allocato preventivamente, in quanto ogni utente può ricoprire più ruoli.
     *
     * @return l'oggetto Judge dell'utente loggato.
     */
    public Judge getJudge() {
        if (judge == null) {
            judge = new Judge(user.getUsername(), user.getPassword(), user.getName(), user.getSurname());
        }
        return judge;
    }

    /**
     * Ritorna l'oggetto ControllerHackathon, che si occupa di gestire le operazioni strettamente legate agli hackathon. Se
     * non esiste, viene preventivamente allocato.
     *
     * @return l'oggetto ControllerHackathon
     */
    public ControllerHackathon getControllerHackathon() {
        if (controllerHackathon == null) {controllerHackathon = new ControllerHackathon();}
        return controllerHackathon;
    }

    /**
     * Ritorna l'oggetto ControllerPlanner, che si occupa di gestire le operazioni strettamente legate all'organizzatore. Se
     * non esiste, viene preventivamente allocato.
     *
     * @return l'oggetto ControllerPlanner
     */
    public ControllerPlanner getControllerPlanner() {
        if (controllerPlanner == null) {controllerPlanner = new ControllerPlanner();}
        return controllerPlanner;
    }

    /**
     * Ritorna l'oggetto ControllerPlayer, che si occupa di gestire le operazioni strettamente legate al partecipante. Se
     * non esiste, viene preventivamente allocato.
     *
     * @return l'oggetto ControllerPlayer
     */
    public ControllerPlayer getControllerPlayer() {
        if (controllerPlayer == null) {controllerPlayer = new ControllerPlayer(this);}
        return controllerPlayer;
    }

    /**
     * Ritorna l'oggetto ControllerJudge, che si occupa di gestire le operazioni strettamente legate al giudice. Se
     * non esiste, viene preventivamente allocato.
     *
     * @return l'oggetto ControllerJudge
     */
    public ControllerJudge getControllerJudge() {
        if (controllerJudge == null) {controllerJudge = new ControllerJudge(this);}
        return controllerJudge;
    }

    /**
     * Ritorna l'oggetto ControllerTeam, che si occupa di gestire le operazioni strettamente legate ai team. Se
     * non esiste, viene preventivamente allocato.
     *
     * @return l'oggetto ControllerTeam
     */
    public ControllerTeam getControllerTeam() {
        if (controllerTeam == null) {controllerTeam = new ControllerTeam(this);}
        return controllerTeam;
    }
}