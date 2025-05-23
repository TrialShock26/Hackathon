package model;
import java.util.ArrayList;

public class Document {
    private String title;
    private String content;
    private ArrayList<String> comments;
    private Team team;
    private ArrayList<Judge> commentators;


    public Document(String title, String content, Team team) {
        this.title = title;
        this.content = content;
        comments = new ArrayList<String>();
        this.team = team;
        commentators = new ArrayList<Judge>();
    }

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getContent() {return content;}
    public void setContent(String content) {this.content = content;}

    public ArrayList<String> getComments() {return comments;}
    public void setComment(String comment) {comments.add(comment);}

    public Team getTeam() {return team;}

    public ArrayList<Judge> getCommentators() {return commentators;}
    public void setCommentators(Judge j) {commentators.add(j);}
}
