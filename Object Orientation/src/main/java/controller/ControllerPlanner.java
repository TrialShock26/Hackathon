package controller;

import dao.PlannerDAO;
import postgresImplementationDao.PlannerImplementationDAO;
import model.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller per la gestione delle operazioni relative agli organizzatori degli hackathon.
 * Gestisce le operazioni di recupero utenti, controllo hackathon e coordinamento delle attività degli organizzatori.
 */
public class ControllerPlanner {
    private Controller controller;

    /**
     * Costruttore della classe ControllerPlanner con relativo riferimento al
     * {@link Controller} padre.
     *
     * @param controller il controller principale dell'applicazione
     */
    public ControllerPlanner(Controller controller) {this.controller = controller;}

    /**
     * Recupera la lista degli utenti della piattaforma, a esclusione
     * dell'organizzatore stesso.
     * Popola la lista {@code allUsernames} con tutti gli username degli utenti.
     *
     * @param planUser     lo username dell'organizzatore
     * @param allUsernames la lista da popolare con gli username degli utenti
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public void controllerGetUsers(String planUser, List<String> allUsernames) throws SQLException {
        PlannerDAO planner = new PlannerImplementationDAO();
        planner.getUsers(planUser, allUsernames);
    }

    /**
     * Recupera la lista degli hackathon organizzati dall'utente attuale.
     * Se la lista è vuota o è richiesto un aggiornamento, interroga il database e popola
     * tutte le liste fornite con i dati degli hackathon. Altrimenti utilizza i dati già memorizzati.
     *
     * @param username            lo username dell'organizzatore per cui recuperare gli hackathon
     * @param titles              la lista da popolare con i titoli degli hackathon
     * @param locations           la lista da popolare con le sedi degli hackathon
     * @param periodOfTime        la lista da popolare con le durate in giorni degli hackathon
     * @param problemDescriptions la lista da popolare con le descrizioni dei problemi
     * @param startDate           la lista da popolare con le date di inizio degli hackathon
     * @param endDate             la lista da popolare con le date di fine degli hackathon
     * @param startSubDate        la lista da popolare con le date di inizio iscrizioni
     * @param endSubDate          la lista da popolare con le date di fine iscrizioni
     * @param maxPlayers          la lista da popolare con i numeri massimi di partecipanti
     * @param maxTeamDim          la lista da popolare con le dimensioni massime dei team
     * @param refreshing          true se si vuole forzare l'aggiornamento dei dati dal database
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public void controllerGetHackathons(String username, List<String> titles, List<String> locations,
                                        List<Long> periodOfTime, List<String> problemDescriptions,
                                        List<Date> startDate, List<Date> endDate,
                                        List<Date> startSubDate, List<Date> endSubDate,
                                        List<Integer> maxPlayers, List<Integer> maxTeamDim,boolean refreshing) throws SQLException {

        if(controller.getPlanner().getHackathons().isEmpty() || refreshing){
            PlannerDAO planner = new PlannerImplementationDAO();
            planner.getHackathons(username, titles, locations,periodOfTime,problemDescriptions,startDate,endDate,startSubDate,endSubDate,maxPlayers,maxTeamDim);
            controller.getPlanner().getHackathons().clear();

            for(int i=0;i<titles.size();i++){
                controller.getPlanner().getHackathons().add(new Hackathon(titles.get(i),locations.get(i),periodOfTime.get(i),
                        startDate.get(i),endDate.get(i),startSubDate.get(i),
                        endSubDate.get(i),maxPlayers.get(i),maxTeamDim.get(i),controller.getPlanner()));
                try {
                    controller.getPlanner().getHackathons().get(i).setProblemDescription(problemDescriptions.get(i));
                } catch (IllegalAccessException e) {
                    return;
                }
            }
        }else{
            for (Hackathon myHackathon : controller.getPlanner().getHackathons()) {
                titles.add(myHackathon.getTitle());
                locations.add(myHackathon.getLocation());
                periodOfTime.add(myHackathon.getPeriodOfTime());
                problemDescriptions.add(myHackathon.getProblemDescription());
                startDate.add(myHackathon.getStartDate());
                endDate.add(myHackathon.getEndDate());
                startSubDate.add(myHackathon.getStartDate());
                endSubDate.add(myHackathon.getEndDate());
                maxPlayers.add(myHackathon.getMaxPlayers());
                maxTeamDim.add(myHackathon.getMaxTeamDim());
            }
        }
    }

    /**
     * Apre un nuovo hackathon con i parametri specificati.
     * Registra l'hackathon nel database associandolo all'organizzatore e ai giudici indicati.
     *
     * @param title           il titolo dell'hackathon
     * @param location        la sede dove si svolge l'hackathon
     * @param startDate       la data di inizio dell'hackathon
     * @param endDate         la data di fine dell'hackathon
     * @param startSubDate    la data di inizio delle iscrizioni
     * @param endSubDate      la data di fine delle iscrizioni
     * @param maxPlayers      il numero massimo di partecipanti
     * @param maxTeamDim      la dimensione massima dei team
     * @param planUsername    lo username dell'organizzatore
     * @param judgesUsernames gli username dei giudici, formattati secondo le necessità del database
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public void controllerOpenHackathon(String title, String location, Date startDate, Date endDate,
                                        Date startSubDate, Date endSubDate, int maxPlayers, int maxTeamDim,
                                        String planUsername , String judgesUsernames) throws SQLException{
        PlannerDAO planner = new PlannerImplementationDAO();
        planner.openHackathon(title,location,startDate,endDate,startSubDate,endSubDate,maxPlayers,maxTeamDim,planUsername,judgesUsernames);
        controller.getPlanner().openHackathon(title, location,  startDate, endDate, startSubDate, endSubDate, maxPlayers, maxTeamDim);
    }

    /**
     * Avvia un hackathon precedentemente aperto. Il database si occuperà di verificare
     * se sono presenti le condizioni necessarie all'avvio.
     *
     * @param title    il titolo dell'hackathon da avviare
     * @param location la sede dell'hackathon da avviare
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public void controllerStartHackathon(String title, String location) throws SQLException {
        PlannerDAO planner = new PlannerImplementationDAO();
        planner.startHackathon(title,location);
    }

    /**
     * Termina un hackathon in corso. Il database effettua tutte le procedure di corretta chiusura
     * e prepara la classifica finale.
     *
     * @param title    il titolo dell'hackathon da terminare
     * @param location la sede dell'hackathon da terminare
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public void controllerEndHackathon(String title, String location) throws SQLException {
        PlannerDAO planner = new PlannerImplementationDAO();
        planner.endHackathon(title,location);
    }
}