package model;
import java.util.ArrayList;

public class Judge extends User {
    private Hackathon myHackathon;
    private ArrayList<Grade> myGradings;
    private ArrayList<Document> myComments;

    public Judge(String username, String password, String name, String surname, Hackathon h) {
        super(username, password, name, surname);
        myHackathon = h;
        myGradings = new ArrayList<Grade>();
        myComments = new ArrayList<Document>();
    }

    public void publishProblem (Hackathon h, String problem) {h.setProblemDescription(problem);}

    public void commentDocument (Document d, String comment) {
        d.setComment(comment);
        myComments.add(d);
        d.setCommentators(this);
    }

    public void gradeTeam(Team t, int value) {
        Grade g = new Grade(this, t, value);
        myGradings.add(g);
        t.setGrades(g);
    }

    public Hackathon getHackathon() {return myHackathon;}
    public ArrayList<Grade> getMyGradings() {return myGradings;}
}