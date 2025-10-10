package dao;

import java.util.ArrayList;

public interface JudgeDAO {
    void publishProblem(String text, String title, String location);
    void examineDocument(String username, String docTitle, String content, String teamName, String hackTitle, String location);
    void gradeTeam(String username, String teamName, String title, String location, int value);
    void getHackathons(String username, ArrayList<String> title, ArrayList<String> location);
    void getTeams(String title, String location, ArrayList<String> teamNames);
    void getDocuments(String teamName, String hackTitle, String location,
                      ArrayList<String> docTitles, ArrayList<String> contents, ArrayList<String> comments);
}