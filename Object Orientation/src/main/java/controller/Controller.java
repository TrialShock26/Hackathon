package controller;

import dao.*;
import postgresImplementationDao.*;

import java.sql.SQLException;

public class Controller {
    private String username;
    private String name;
    private String surname;
    private ControllerHackathon hackathon;
    private ControllerPlanner planner;
    private ControllerPlayer player;
    private ControllerTeam team;

    public Controller() {
        hackathon = null;
        planner = null;
        player = null;
        team = null;
    }

    public boolean login(String username, String password) {
        boolean success = false;
        UserDAO user = new UserImplementationDAO();

        try {
            success = user.login(username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (success) {
            this.username = username;
            this.name = username;
            this.surname = username;
        }
        return success;
    }

    public boolean newUser(String username, String name, String surname, String password) {
        UserDAO user = new UserImplementationDAO();
        boolean success = true;

        try {
            user.newUser(username, name, surname, password);
        } catch (SQLException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    public String getUsername() {return username;}
    public String getName() {return name;}
    public String getSurname() {return surname;}
    public ControllerHackathon getHackathon() {return hackathon;}
    public ControllerPlanner getPlanner() {return planner;}
    public ControllerPlayer getPlayer() {return player;}
    public ControllerTeam getTeam() {return team;}
}