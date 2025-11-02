package controller;

import dao.TeamDAO;
import postgresImplementationDao.TeamImplementationDAO;
import model.Document;
import model.Hackathon;
import model.Team;

import java.sql.SQLException;
import java.util.ArrayList;

public class ControllerTeam {
    private Controller controller;
    private ArrayList<Team> teams;

    public ControllerTeam(Controller controller) {this.controller = controller;}

    public void controllerPublishProgress(String teamName, String hackTitle, String location, String docTitle, String content) {
        try{

            TeamDAO teamController = new TeamImplementationDAO();

            teamController.publishProgress(teamName,hackTitle,location,docTitle,content);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void controllerGetDocuments(String teamName, String title, String location,
                                       ArrayList<String> documentTitles, ArrayList<String> documentContents,
                                       ArrayList<String> documentComments, boolean refreshing) throws SQLException {
        if (teams == null || refreshing) {
            TeamDAO teamDB = new TeamImplementationDAO();
            teamDB.getDocuments(teamName, title, location, documentTitles, documentContents, documentComments);
            teams = new ArrayList<>();
            Team t = new Team(teamName, controller.getPlayer(), new Hackathon(title, location, 0, null, null, null, null, 0, 0, null));
            for (int i = 0; i < documentTitles.size(); i++) {
                Document d = new Document(documentTitles.get(i), documentContents.get(i), t);
                d.setComment(documentComments.get(i));
                t.getProgress().add(d);
            }
            teams.add(t);
        } else {
            int counter = 0;
            for (Team t : teams) {
                if (t.getName().equals(teamName) && t.getHackathon().getTitle().equals(title) &&
                        t.getHackathon().getLocation().equals(location)) {
                    for (Document d : t.getProgress()) {
                        documentTitles.add(d.getTitle());
                        documentContents.add(d.getContent());
                        documentComments.add(d.getComment());
                    }
                    return;
                } else {counter++;}
            }
            if (counter == teams.size()) {
                TeamDAO teamDB = new TeamImplementationDAO();
                teamDB.getDocuments(teamName, title, location, documentTitles, documentContents, documentComments);
                Team t = new Team(teamName, controller.getPlayer(), new Hackathon(title, location, 0, null, null, null, null, 0, 0, null));
                for (int i = 0; i < documentTitles.size(); i++) {
                    Document d = new Document(documentTitles.get(i), documentContents.get(i), t);
                    d.setComment(documentComments.get(i));
                    t.getProgress().add(d);
                }
                teams.add(t);
            }
        }
    }

    public ArrayList<Team> getTeams() {return teams;}
}