package controller;

import dao.JudgeDAO;
import postgresImplementationDao.JudgeImplementationDAO;
import model.*;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Controller per la gestione delle operazioni relative ai giudici.
 * Questa classe funge da intermediario tra l'interfaccia utente e il livello di accesso ai dati,
 * gestendo le operazioni che un giudice può eseguire come visualizzare hackathon,
 * pubblicare problemi, esaminare team e documenti, e assegnare valutazioni.
 */
public class ControllerJudge {
    private Controller controller;

    /**
     * Instanzia un nuovo oggetto ControllerJudge, preservando un riferimento al Controller "padre".
     *
     * @param controller il controller "padre".
     */
    public ControllerJudge(Controller controller) {this.controller = controller;}

    /**
     * Recupera la lista degli hackathon a cui il giudice è assegnato.
     * Se le selezioni sono vuote o si richiede un aggiornamento, interroga il database
     * e aggiorna la lista delle selezioni del giudice. Altrimenti restituisce i dati già presenti in memoria.
     *
     * @param username            il nome utente del giudice
     * @param titles              lista da popolare con i titoli degli hackathon
     * @param locations           lista da popolare con le sedi degli hackathon
     * @param problemDescriptions lista da popolare con le descrizioni dei problemi da affrontare
     * @param refreshing          true per forzare l'aggiornamento dal database, false per usare i dati in cache
     * @throws SQLException           se si verifica un errore durante l'accesso al database
     * @throws IllegalAccessException se si verifica un errore di accesso illegale
     */
    public void controllerGetHackathons(String username, ArrayList<String> titles, ArrayList<String> locations,
                                        ArrayList<String> problemDescriptions, boolean refreshing) throws SQLException, IllegalAccessException {
        if (controller.getJudge().getSelections().isEmpty() || refreshing) {
            JudgeDAO judgeDB = new JudgeImplementationDAO();
            judgeDB.getHackathons(username, titles, locations, problemDescriptions);
            controller.getJudge().getSelections().clear();
            for (int i = 0; i < titles.size(); i++) {
                Hackathon h = new Hackathon(titles.get(i), locations.get(i), 0, null, null, null, null, 0, 0, null);
                h.setProblemDescription(problemDescriptions.get(i));
                Selection s = new Selection(null, h);
                s.setJudge(controller.getJudge());
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

    /**
     * Pubblica la descrizione del problema per uno specifico hackathon.
     * Aggiorna sia il database che il modello in memoria.
     *
     * @param title              il titolo dell'hackathon
     * @param location           la località dell'hackathon
     * @param problemDescription la descrizione del problema da pubblicare
     * @throws SQLException           se si verifica un errore durante l'accesso al database
     * @throws IllegalAccessException se si prova a modificare un problema già impostato precedentemente
     */
    public void controllerPublishProblem(String title, String location, String problemDescription) throws SQLException, IllegalAccessException {
        JudgeDAO judgeDB = new JudgeImplementationDAO();
        judgeDB.publishProblem(problemDescription, title, location);
        for (Selection myS : controller.getJudge().getSelections()) {
            if(myS.getHackathon().getTitle().equals(title) && myS.getHackathon().getLocation().equals(location)) {
                controller.getJudge().publishProblem(myS.getHackathon(), problemDescription);
            }
        }
    }

    /**
     * Recupera la lista dei team partecipanti a uno specifico hackathon.
     * Se richiesto l'aggiornamento o se la lista è vuota, interroga il database,
     * altrimenti restituisce i dati già presenti in memoria.
     *
     * @param title      il titolo dell'hackathon
     * @param location   la sede dell'hackathon
     * @param teamNames  lista da popolare con i nomi dei team
     * @param refreshing true per forzare l'aggiornamento dal database, false per usare i dati in cache
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public void controllerGetTeams(String title, String location, ArrayList<String> teamNames, boolean refreshing) throws SQLException {
        Hackathon h = findHackathon(title, location);
        if (h == null) {return;}
        if (refreshing || h.getTeams().isEmpty()) {
            JudgeDAO judgeDB = new JudgeImplementationDAO();
            judgeDB.getTeams(title, location, teamNames);
            h.getTeams().clear();
            for (String teamName : teamNames) {
                h.getTeams().add(new Team(teamName, null, h));
            }
        } else {
            for (Team t : h.getTeams()) {
                teamNames.add(t.getName());
            }
        }
    }

    /**
     * Trova un hackathon nelle selezioni del giudice in base al titolo e alla sede.
     *
     * @param title    il titolo dell'hackathon da cercare
     * @param location la sede dell'hackathon da cercare
     * @return l'hackathon trovato (il valore null non è mai ritornato in quanto può leggere solo dati già presenti in cache)
     */
    private Hackathon findHackathon(String title, String location) {
        for (Selection s : controller.getJudge().getSelections()) {
            Hackathon h = s.getHackathon();
            if (h.getTitle().equals(title) && h.getLocation().equals(location)) {
                return h;
            }
        }
        return null;
    }

    /**
     * Esamina un documento di un team e aggiunge un commento.
     * Aggiorna sia il database che il modello in memoria con il commento del giudice.
     *
     * @param username   il nome utente del giudice
     * @param docTitle   il titolo del documento da esaminare
     * @param content    il contenuto del documento
     * @param oldComment il commento precedente del documento
     * @param teamName   il nome del team proprietario del documento
     * @param hackTitle  il titolo dell'hackathon
     * @param location   la sede dell'hackathon
     * @param text       il nuovo commento da aggiungere al documento
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
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

    /**
     * Assegna una valutazione a un team per uno specifico hackathon.
     * Aggiorna sia il database che il modello in memoria con il punteggio assegnato.
     *
     * @param username il nome utente del giudice
     * @param teamName il nome del team da valutare
     * @param title    il titolo dell'hackathon
     * @param location la sede dell'hackathon
     * @param value    il punteggio da assegnare al team
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
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