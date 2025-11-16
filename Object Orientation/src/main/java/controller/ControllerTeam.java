package controller;

import dao.TeamDAO;
import postgresImplementationDao.TeamImplementationDAO;
import model.Document;
import model.Hackathon;
import model.Team;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller per la gestione delle operazioni relative ai team negli hackathon.
 * Questa classe gestisce la pubblicazione dei progressi dei team, il recupero
 * dei documenti associati e la gestione della cache dei team e dei loro contenuti.
 */
public class ControllerTeam {
    private Controller controller;
    private ArrayList<Team> teams;

    /**
     * Costruttore della classe ControllerTeam con relativo riferimento al
     * {@link Controller} padre.
     *
     * @param controller il controller principale dell'applicazione
     */
    public ControllerTeam(Controller controller) {this.controller = controller;}

    /**
     * Pubblica un documento di progresso per un team specifico in un hackathon.
     * Il documento viene registrato nel database e associato
     * nella memoria locale al team e all'hackathon indicati.
     *
     * @param teamName  il nome del team
     * @param hackTitle il titolo dell'hackathon
     * @param location  la sede dell'hackathon
     * @param docTitle  il titolo del documento da pubblicare
     * @param content   il contenuto del documento
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public void controllerPublishProgress(String teamName, String hackTitle, String location, String docTitle, String content) throws SQLException {
        TeamDAO teamDAO = new TeamImplementationDAO();
        teamDAO.publishProgress(teamName, hackTitle, location, docTitle, content);

        for (Team t : controller.getPlayer().getTeams()) {
            if (t.getName().equals(teamName) &&
                t.getHackathon().getTitle().equals(hackTitle) && t.getHackathon().getLocation().equals(location)) {
                t.publishProgress(content, docTitle);
                return;
            }
        }
    }

    /**
     * Recupera la lista dei documenti di progresso pubblicati da un team per un hackathon.
     * Se i dati non sono in cache o è richiesto un aggiornamento, li recupera dal database.
     * Altrimenti cerca il team nella memoria locale e restituisce i documenti memorizzati.
     *
     * @param teamName         il nome del team
     * @param title            il titolo dell'hackathon
     * @param location         la sede dell'hackathon
     * @param documentTitles   la lista da popolare con i titoli dei documenti
     * @param documentContents la lista da popolare con i contenuti dei documenti
     * @param documentComments la lista da popolare con i commenti sui documenti
     * @param refreshing       true se si vuole forzare l'aggiornamento dei dati dal database
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public void controllerGetDocuments(String teamName, String title, String location,
                                       List<String> documentTitles, List<String> documentContents,
                                       List<String> documentComments, boolean refreshing) throws SQLException {
        if (teams == null || refreshing) {
            TeamDAO teamDB = new TeamImplementationDAO();
            teamDB.getDocuments(teamName, title, location, documentTitles, documentContents, documentComments);
            teams = new ArrayList<>();
            Team t = new Team(teamName, null, new Hackathon(title, location));
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
                t = new Team(teamName, null, new Hackathon(title, location));
                for (int i = 0; i < documentTitles.size(); i++) {
                    Document d = new Document(documentTitles.get(i), documentContents.get(i), t);
                    d.setComment(documentComments.get(i));
                    t.getProgress().add(d);
                }
                teams.add(t);
            }
        }
    }

    /**
     * Cerca un team specifico nella memoria locale utilizzando nome, titolo hackathon e sede.
     * Metodo privato di supporto per la gestione della cache dei team.
     *
     * @param teamName il nome del team da cercare
     * @param title    il titolo dell'hackathon associato al team
     * @param location la sede dell'hackathon associato al team
     * @return il team trovato, oppure null se non presente nella cache
     */
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
     * Restituisce la lista di tutti i team memorizzati nella cache.
     *
     * @return la lista dei team
     */
    public List<Team> getTeams() {return teams;}
}