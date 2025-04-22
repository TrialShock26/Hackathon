package model;

public class Grade {
    private int value;
    private Judge grader;
    private Team graded;

    public Grade(Judge grader, Team graded, int value) {
        this.value = value;
        this.grader = grader;
        this.graded = graded;
    }

    public int getValue() {return value;}
    public void setValue(int value) {this.value = value;}

    public Judge getGrader() {return grader;}

    public Team getGraded() {return graded;}
}
