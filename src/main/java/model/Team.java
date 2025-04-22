package model;
import java.util.ArrayList;

public class Team {
    private String name;
    private ArrayList<Player> playerList = new ArrayList<Player>();
    private ArrayList<Hackathon> hackathonsDone = new ArrayList<Hackathon>();
    private ArrayList<Document> progressList = new ArrayList<Document>();
    private ArrayList<Grade> grades = new ArrayList<Grade>();

    public Team(String teamName, Player p) {
        name = teamName;
        playerList.add(p);
    }

    public void publishProgress (String title, String content) { //setter for progressList field
        Document d = new Document(title, content, this);
        progressList.add(d);
    }

    public String getName () {return name;}
    public void setName (String name) {this.name = name;}

    public ArrayList<Player> getPlayers() {return playerList;}
    public void setPlayer (Player p) {playerList.add(p);}

    public ArrayList<Hackathon> getHackathonsDone() {return hackathonsDone;}
    public void setHackathonsDone(Hackathon h) {hackathonsDone.add(h);}

    public ArrayList<Document> getProgress () {return progressList;}

    public ArrayList<Grade> getGrades() {return grades;}
    public void setGrades (Grade g) {grades.add(g);}

}