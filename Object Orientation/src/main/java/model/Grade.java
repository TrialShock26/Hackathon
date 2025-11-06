package model;

/**
 * Rappresenta un singolo voto assegnato da un giudice
 * a un team. Memorizza il valore numerico del voto e le entità coinvolte
 * (chi ha votato e chi è stato votato).
 */
public class Grade {
    private int value;
    private Judge grader;
    private Team graded;

    /**
     * Instanzia un nuovo Grade con i parametri necessari.
     *
     * @param grader il giudice che assegna il voto.
     * @param graded il team che riceve il voto.
     * @param value  il valore numerico del voto.
     */
    public Grade(Judge grader, Team graded, int value) {
        this.value = value;
        this.grader = grader;
        this.graded = graded;
    }

    /**
     * Restituisce il valore numerico del voto.
     *
     * @return il valore del voto.
     */
    public int getValue() {return value;}

    /**
     * Restituisce il giudice che ha assegnato il voto.
     *
     * @return il Judge votante.
     */
    public Judge getGrader() {return grader;}

    /**
     * Restituisce il team che ha ricevuto il voto.
     *
     * @return il Team votato.
     */
    public Team getGraded() {return graded;}
}