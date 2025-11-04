package model;

import java.util.ArrayList;

/**
 * Rappresenta un giudice che partecipa a un hackathon con il compito
 * di valutare i team, commentare i documenti e pubblicare la descrizione del problema proposto.
 * <p>
 * Ogni giudice può essere associato a una o più selezioni (per hackathon differenti), può assegnare voti ai team
 * e lasciare commenti sui documenti presentati dai partecipanti.
 */
public class Judge extends User {
    private ArrayList<Selection> mySelections;
    private ArrayList<Grade> myGradings;
    private ArrayList<Document> commentDone;

    /**
     * Costruisce un nuovo oggetto {@code Judge} con le informazioni di base da Utente
     * e allocando le varie liste per i dati in cache.
     *
     * @param username il nome utente del giudice
     * @param password la password del giudice
     * @param name     il nome del giudice
     * @param surname  il cognome del giudice
     */
    public Judge(String username, String password, String name, String surname) {
        super(username, password, name, surname);
        mySelections = new ArrayList<>();
        myGradings = new ArrayList<>();
        commentDone = new ArrayList<>();
    }

    /**
     * Pubblica la descrizione del problema per un hackathon specifico.
     * <p>
     * Il giudice può impostare la descrizione del problema solo se non è già stata definita.
     *
     * @param h       l'hackathon su cui pubblicare il problema
     * @param problem la descrizione del problema da pubblicare
     * @throws IllegalAccessException se la descrizione del problema è già stata impostata
     */
    public void publishProblem (Hackathon h, String problem) throws IllegalAccessException {
        h.setProblemDescription(problem);
    }

    /**
     * Aggiunge un commento a un documento e registra l'azione tra i commenti effettuati dal giudice.
     *
     * @param d       il documento da commentare
     * @param comment il testo del commento
     */
    public void commentDocument (Document d, String comment) {
        d.setComment(comment);
        commentDone.add(d);
        d.setCommentators(this);
    }

    /**
     * Assegna un voto a un team e registra la valutazione tra quelle effettuate dal giudice.
     *
     * @param t     il team da valutare
     * @param value il valore numerico del voto assegnato
     */
    public void gradeTeam(Team t, int value) {
        Grade g = new Grade(this, t, value);
        myGradings.add(g);
        t.setGrades(g);
    }

    /**
     * Restituisce la lista delle selezioni a cui il giudice partecipa.
     *
     * @return la lista delle selezioni
     */
    public ArrayList<Selection> getSelections() {return mySelections;}

    /**
     * Aggiunge una selezione alla lista di quelle assegnate al giudice.
     *
     * @param inSel la selezione da aggiungere
     */
    public void setSelections(Selection inSel) {mySelections.add(inSel);}

    /**
     * Restituisce la lista delle valutazioni assegnate dal giudice.
     *
     * @return la lista delle valutazioni
     */
    public ArrayList<Grade> getGradings() {return myGradings;}
}
