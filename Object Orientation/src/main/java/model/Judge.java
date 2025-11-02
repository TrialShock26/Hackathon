package model;

import java.util.ArrayList;

public class Judge extends User {
    private ArrayList<Selection> mySelections;
    private ArrayList<Grade> myGradings;
    private ArrayList<Document> commentDone;

    public Judge(String username, String password, String name, String surname) {
        super(username, password, name, surname);
        mySelections = new ArrayList<>();
        myGradings = new ArrayList<>();
        commentDone = new ArrayList<>();
    }

    public void publishProblem (Hackathon h, String problem) throws IllegalAccessException {
        h.setProblemDescription(problem);
    }

    public void commentDocument (Document d, String comment) {
        d.setComment(comment);
        commentDone.add(d);
        d.setCommentators(this);
    }

    public void gradeTeam(Team t, int value) {
        Grade g = new Grade(this, t, value);
        myGradings.add(g);
        t.setGrades(g);
    }

    public ArrayList<Selection> getSelections() {return mySelections;}
    public void setSelections(Selection inSel) {mySelections.add(inSel);}

    public ArrayList<Grade> getGradings() {return myGradings;}
}