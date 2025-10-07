package model;

import java.util.ArrayList;

public class Document {
    private String title;
    private String content;
    private String comment;
    private Team team;
    private ArrayList<Judge> commentators;


    public Document(String title, String content, Team team) {
        this.title = title;
        this.content = content;
        comment = "Commento assente.";
        this.team = team;
        commentators = new ArrayList<Judge>();
    }

    public String getTitle() {return title;}
    public String getContent() {return content;}

    public String getComments() {return comment;}
    public void setComment(String inComm) {
        if (comment == "Commento assente.") {comment = inComm;}
        else {comment = comment + "\n" + inComm;}
    }

    public Team getTeam() {return team;}

    public ArrayList<Judge> getCommentators() {return commentators;}
    public void setCommentators(Judge j) {commentators.add(j);}
}
