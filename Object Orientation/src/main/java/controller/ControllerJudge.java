package controller;

import dao.JudgeDAO;
import model.*;
import postgresImplementationDao.JudgeImplementationDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public class ControllerJudge {
    private Controller controller;
    private ArrayList<Selection> selections;

    public ControllerJudge(Controller controller) {this.controller = controller;}

    public void controllerGetHackathons(String username, ArrayList<String> titles, ArrayList<String> locations,
                                            ArrayList<String> problemDescriptions, boolean refreshing) throws SQLException, IllegalAccessException{
        if (selections == null || refreshing) {
            JudgeDAO judgeDB = new JudgeImplementationDAO();
            judgeDB.getHackathons(username, titles, locations, problemDescriptions);
            selections = new ArrayList<Selection>();
            for (int i = 0; i < titles.size(); i++) {
                Hackathon h = new Hackathon(titles.get(i), locations.get(i), 0, null, null, null, null, 0, 0, null);
                h.setProblemDescription(problemDescriptions.get(i));
                Selection s = new Selection(null, h);
                s.setJudges(controller.getJudge());
                selections.add(s);
            }
        } else {
            for (Selection myS : selections) {
                titles.add(myS.getHackathon().getTitle());
                locations.add(myS.getHackathon().getLocation());
                problemDescriptions.add(myS.getHackathon().getProblemDescription());
            }
        }
    }

    public void controllerSetProblemDescription(String title, String location, String problemDescription) throws SQLException, IllegalAccessException {
        JudgeDAO judgeDB = new JudgeImplementationDAO();
        judgeDB.publishProblem(problemDescription, title, location);
        for (Selection myS : selections) {
            if(myS.getHackathon().getTitle().equals(title) && myS.getHackathon().getLocation().equals(location)) {
                myS.getHackathon().setProblemDescription(problemDescription);
            }
        }
    }
}
