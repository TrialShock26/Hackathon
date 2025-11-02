package controller;

import dao.JudgeDAO;
import postgresImplementationDao.JudgeImplementationDAO;
import model.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControllerJudge {
    private Controller controller;

    public ControllerJudge(Controller controller) {this.controller = controller;}

    public void controllerGetHackathons(String username, ArrayList<String> titles, ArrayList<String> locations,
                                            ArrayList<String> problemDescriptions, boolean refreshing) throws SQLException, IllegalAccessException{
        if (controller.getJudge().getSelections().isEmpty() || refreshing) {
            JudgeDAO judgeDB = new JudgeImplementationDAO();
            judgeDB.getHackathons(username, titles, locations, problemDescriptions);
            controller.getJudge().getSelections().clear();
            for (int i = 0; i < titles.size(); i++) {
                Hackathon h = new Hackathon(titles.get(i), locations.get(i), 0, null, null, null, null, 0, 0, null);
                h.setProblemDescription(problemDescriptions.get(i));
                Selection s = new Selection(null, h);
                s.setJudges(controller.getJudge());
                controller.getJudge().getSelections().add(s);
            }
        } else {
            for (Selection myS : controller.getJudge().getSelections()) {
                titles.add(myS.getHackathon().getTitle());
                locations.add(myS.getHackathon().getLocation());
                problemDescriptions.add(myS.getHackathon().getProblemDescription());
            }
        }
    }

    public void controllerPublishProblem(String title, String location, String problemDescription) throws SQLException, IllegalAccessException {
        JudgeDAO judgeDB = new JudgeImplementationDAO();
        judgeDB.publishProblem(problemDescription, title, location);
        for (Selection myS : controller.getJudge().getSelections()) {
            if(myS.getHackathon().getTitle().equals(title) && myS.getHackathon().getLocation().equals(location)) {
                controller.getJudge().publishProblem(myS.getHackathon(), problemDescription);
            }
        }
    }

    public void controllerGetTeams(String title, String location, ArrayList<String> teamNames, boolean refreshing) throws SQLException, IllegalAccessException {
        for (Selection s : controller.getJudge().getSelections()) {
            Hackathon h = s.getHackathon();
            if (h.getTitle().equals(title) && h.getLocation().equals(location)) {
                if (refreshing || h.getTeams().isEmpty()) {
                    JudgeDAO judgeDB = new JudgeImplementationDAO();
                    judgeDB.getTeams(title, location, teamNames);
                    h.getTeams().clear();
                    for (int i = 0; i < teamNames.size(); i++) {
                        h.getTeams().add(new Team(teamNames.get(i), null, h));
                    }
                } else {
                    for (Team t : h.getTeams()) {
                        teamNames.add(t.getName());
                    }
                }
                return;
            }
        }
    }

    public void controllerExamineDocument(String username, String docTitle, String content, String oldComment,
                                          String teamName, String hackTitle, String location, String text) throws SQLException {
        JudgeDAO judgeDB = new JudgeImplementationDAO();
        judgeDB.examineDocument(username, docTitle, content, teamName, hackTitle, location, text);
        for (Team t : controller.getControllerTeam().getTeams()) {
            if (t.getName().equals(teamName) && t.getHackathon().getTitle().equals(hackTitle) &&
                    t.getHackathon().getLocation().equals(location)) {
                for (Document d : t.getProgress()) {
                    if (d.getTitle().equals(docTitle) && d.getComment().equals(oldComment)) {
                        controller.getJudge().commentDocument(d, text);
                        return;
                    }
                }
            }
        }
    }

    public void controllerGradeTeam(String username, String teamName, String title, String location, int value) throws SQLException {
        JudgeDAO judgeDB = new JudgeImplementationDAO();
        judgeDB.gradeTeam(username, teamName, title, location, value);
        for (Team t : controller.getControllerTeam().getTeams()) {
            if (t.getName().equals(teamName) && t.getHackathon().getTitle().equals(title) &&
                    t.getHackathon().getLocation().equals(location)) {
                controller.getJudge().gradeTeam(t, value);
            }
        }
    }
}
