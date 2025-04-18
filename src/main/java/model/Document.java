package model;
import java.util.ArrayList;

public class Document {
    String title;
    String content;
    ArrayList<String> comments = new ArrayList<String>();

    public Document(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getContent() {return content;}
    public void setContent(String content) {this.content = content;}

    public ArrayList<String> getComments() {return comments;}
    public void setComment(String comment) {comments.add(comment);}
}
