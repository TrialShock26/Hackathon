package controller;

import dao.*;
import postgresImplementationDao.*;
import model.*;

import java.sql.SQLException;

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

    public boolean login(String username, String password) {
        String nameSurname = null;
        UserDAO userDB = new UserImplementationDAO();

        try {
            nameSurname = userDB.login(username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (nameSurname != null) {
            int idx = nameSurname.indexOf('@');
            user = new User(username, password, nameSurname.substring(0, idx), nameSurname.substring(idx+1));
            return true;
        }
        return false;
    }

    public boolean newUser(String username, String name, String surname, String password) {
        UserDAO userDB = new UserImplementationDAO();
        boolean success = true;

        try {
            userDB.newUser(username, name, surname, password);
            user = new User(username, password, name, surname);
        } catch (SQLException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    public User getUser() {return user;}

    public Player getPlayer() {
        if (player == null) {
            player = new Player(user.getUsername(), user.getPassword(), user.getName(), user.getSurname());
        }
        return player;
    }

    public Planner getPlanner() {
        if (planner == null) {
            planner = new Planner(user.getUsername(), user.getPassword(), user.getName(), user.getSurname());
        }
        return planner;
    }

    public Judge getJudge() {
        if (judge == null) {
            judge = new Judge(user.getUsername(), user.getPassword(), user.getName(), user.getSurname());
        }
        return judge;
    }

    public ControllerHackathon getControllerHackathon() {
        if (controllerHackathon == null) {controllerHackathon = new ControllerHackathon();}
        return controllerHackathon;
    }
    public ControllerPlanner getControllerPlanner() {
        if (controllerPlanner == null) {controllerPlanner = new ControllerPlanner();}
        return controllerPlanner;
    }
    public ControllerPlayer getControllerPlayer() {
        if (controllerPlayer == null) {controllerPlayer = new ControllerPlayer(this);}
        return controllerPlayer;
    }
    public ControllerTeam getControllerTeam() {
        if (controllerTeam == null) {controllerTeam = new ControllerTeam();}
        return controllerTeam;
    }
    public ControllerJudge getControllerJudge() {
        if (controllerJudge == null) {controllerJudge = new ControllerJudge(this);}
        return controllerJudge;
    }
}