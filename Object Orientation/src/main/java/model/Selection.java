package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta il processo di selezione dei {@link Judge} da parte di un {@link Planner} per un determinato {@link Hackathon}.
 * <p>
 * Ogni istanza di {@code Selection} associa un organizzatore, un hackathon
 * e una lista di giudici incaricati di valutare i team nella competizione.
 */
public class Selection {
    private Planner planner;
    private Hackathon hackathon;
    private ArrayList<Judge> judges;

    /**
     * Crea una nuova istanza di {@code Selection} associando un {@link Planner}
     * e un {@link Hackathon}, inizializzando la lista dei giudici come vuota.
     *
     * @param inPlanner   l'organizzatore responsabile della selezione
     * @param inHackathon l'hackathon per il quale avviene la selezione
     */
    public Selection(Planner inPlanner, Hackathon inHackathon) {
        planner = inPlanner;
        hackathon = inHackathon;
        judges = new ArrayList<>();
    }

    /**
     * Restituisce il {@link Planner} che ha effettuato la selezione.
     *
     * @return l'organizzatore associato
     */
    public Planner getPlanner() {return planner;}

    /**
     * Restituisce l'{@link Hackathon} a cui si riferisce la selezione.
     *
     * @return l'hackathon associato
     */
    public Hackathon getHackathon() {return hackathon;}

    /**
     * Restituisce la lista dei {@link Judge} selezionati per l'hackathon.
     *
     * @return la lista dei giudici
     */
    public List<Judge> getJudges() {return judges;}

    /**
     * Aggiunge un nuovo {@link Judge} alla lista dei giudici selezionati.
     *
     * @param inJudge il giudice da aggiungere
     */
    public void setJudge(Judge inJudge) {judges.add(inJudge);}
}
