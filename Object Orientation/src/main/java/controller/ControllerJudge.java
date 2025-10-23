package controller;

import dao.JudgeDAO;
import model.*;
import postgresImplementationDao.JudgeImplementationDAO;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControllerJudge {
    public void controllerGetHackathons(String username, ArrayList<String> titles, ArrayList<String> locations) throws SQLException {
        JudgeDAO judgeDB = new JudgeImplementationDAO();
        judgeDB.getHackathons(username, titles, locations);
    }
}
