package controller; //TODO javadoc

import dao.TeamDAO;
import postgresImplementationDao.TeamImplementationDAO;
import model.Document;
import model.Hackathon;
import model.Team;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * The type Controller team.
 */
public class ControllerTeam {
    private Controller controller;
    private ArrayList<Team> teams;

    /**
     * Instantiates a new Controller team.
     *
     * @param controller the controller
     */
    public ControllerTeam(Controller controller) {
        this.controller = controller;
    }

    /**
     * Controller publish progress.
     *
     * @param teamName  the team name
     * @param hackTitle the hack title
     * @param location  the location
     * @param docTitle  the doc title
     * @param content   the content
     * @throws SQLException the sql exception
     */
    public void controllerPublishProgress(String teamName, String hackTitle, String location, String docTitle, String content) throws SQLException {
        TeamDAO teamController = new TeamImplementationDAO();
        teamController.publishProgress(teamName, hackTitle, location, docTitle, content);
    }

    /**
     * Controller get documents.
     *
     * @param teamName         the team name
     * @param title            the title
     * @param location         the location
     * @param documentTitles   the document titles
     * @param documentContents the document contents
     * @param documentComments the document comments
     * @param refreshing       the refreshing
     * @throws SQLException the sql exception
     */
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
            Team t = findTeam(teamName, title, location);
            if (t != null) {
                for (Document d : t.getProgress()) {
                    documentTitles.add(d.getTitle());
                    documentContents.add(d.getContent());
                    documentComments.add(d.getComment());
                }
            } else {
                TeamDAO teamDB = new TeamImplementationDAO();
                teamDB.getDocuments(teamName, title, location, documentTitles, documentContents, documentComments);
                t = new Team(teamName, controller.getPlayer(), new Hackathon(title, location, 0, null, null, null, null, 0, 0, null));
                for (int i = 0; i < documentTitles.size(); i++) {
                    Document d = new Document(documentTitles.get(i), documentContents.get(i), t);
                    d.setComment(documentComments.get(i));
                    t.getProgress().add(d);
                }
                teams.add(t);
            }
        }
    }

    private Team findTeam(String teamName, String title, String location) {
        for (Team t : teams) {
            if (t.getName().equals(teamName) && t.getHackathon().getTitle().equals(title) &&
                    t.getHackathon().getLocation().equals(location)) {
                return t;
            }
        }
        return null;
    }

    /**
     * Gets teams.
     *
     * @return the teams
     */
    public ArrayList<Team> getTeams() {return teams;}
}