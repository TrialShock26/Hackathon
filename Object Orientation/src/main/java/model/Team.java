package model;

import java.util.ArrayList;

public class Team {
    private String name;
    private int membersNumber;
    private ArrayList<Player> playerList;
    private Hackathon hackathonMember;
    private ArrayList<Document> progressList;
    private ArrayList<Grade> grades;

    public Team(String teamName, Player p, Hackathon hack) {
        name = teamName;
        membersNumber = 1;
        hackathonMember = hack;
        playerList = new ArrayList<>();
        playerList.add(p);
        progressList = new ArrayList<>();
        grades = new ArrayList<>();
    }

    public void publishProgress (String title, String content) {
        Document d = new Document(title, content, this);
        progressList.add(d);
    }

    public String getName () {return name;}

    public Hackathon getHackathon() {return hackathonMember;}
    public int getMembersNumber() {return membersNumber;}

    public ArrayList<Player> getPlayers() {return playerList;}
    public void setPlayer (Player p) {
        playerList.add(p);
        membersNumber++;
    }

    public void playerLeaving(Player p) {
        playerList.remove(p);
        membersNumber--;
    }

    public ArrayList<Document> getProgress () {return progressList;}

    public ArrayList<Grade> getGrades() {return grades;}
    public void setGrades (Grade g) {grades.add(g);}

}