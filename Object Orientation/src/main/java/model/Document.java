package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta un documento generico all'interno del sistema.
 * Ogni documento è associato a un team e può contenere un titolo, un contenuto,
 * un commento e una lista di giudici che hanno commentato il documento.
 */
public class Document {
    private String title;
    private String content;
    private String comment;
    private Team team;
    private ArrayList<Judge> commentators;

    /**
     * Instanzia un nuovo Document.
     * Il commento è impostato a un valore di default, similmente a come accade sul database
     *
     * @param title   il titolo del documento.
     * @param content il contenuto testuale del documento.
     * @param team    il team a cui è associato il documento.
     */
    public Document(String title, String content, Team team) {
        this.title = title;
        this.content = content;
        comment = "Commento assente.";
        this.team = team;
        commentators = new ArrayList<>();
    }

    /**
     * Restituisce il titolo del documento.
     *
     * @return il titolo del documento.
     */
    public String getTitle() {return title;}

    /**
     * Restituisce il contenuto testuale del documento.
     *
     * @return il contenuto del documento.
     */
    public String getContent() {return content;}

    /**
     * Restituisce il commento attuale del documento.
     *
     * @return il commento del documento.
     */
    public String getComment() {return comment;}

    /**
     * Imposta o aggiunge in coda un commento al documento. Se è il primo commento, lo imposta;
     * altrimenti, lo aggiunge al commento esistente su una nuova riga.
     *
     * @param inComm la stringa di commento da aggiungere.
     */
    public void setComment(String inComm) {
        if (comment.equals("Commento assente.")) {comment = inComm;}
        else {comment = comment + "\n" + inComm;}
    }

    /**
     * Restituisce il Team a cui è associato il documento.
     *
     * @return il Team associato.
     */
    public Team getTeam() {return team;}

    /**
     * Restituisce la lista dei giudici che hanno commentato il documento.
     *
     * @return la lista di Judge.
     */
    public List<Judge> getCommentators() {return commentators;}

    /**
     * Aggiunge un giudice alla lista dei commentatori del documento.
     *
     * @param j l'oggetto Judge da aggiungere come commentatore.
     */
    public void setCommentators(Judge j) {commentators.add(j);}
}