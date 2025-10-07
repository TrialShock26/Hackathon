package model;

import java.util.ArrayList;

public class Selection {
    private Planner planner;
    private Hackathon hackathon;
    private ArrayList<Judge> judges;

    public Selection(Planner inPlanner, Hackathon inHackathon) {
        planner = inPlanner;
        hackathon = inHackathon;
        judges = new ArrayList<Judge>();
    }

    public Planner getPlanner() {return planner;}
    public Hackathon getHackathon() {return hackathon;}
    public ArrayList<Judge> getJudges() {return judges;}
    public void setJudges(Judge inJudge) {judges.add(inJudge);}
}
