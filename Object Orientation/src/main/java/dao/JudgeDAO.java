package dao;

import java.sql.SQLException;
import java.util.ArrayList;

public interface JudgeDAO {
    void publishProblem(String text, String title, String location) throws SQLException;
    void examineDocument(String username, String docTitle, String content,
                         String teamName, String hackTitle, String location, String text) throws SQLException;
    void gradeTeam(String username, String teamName, String title, String location, int value) throws SQLException;
    void getHackathons(String username, ArrayList<String> titles, ArrayList<String> locations,
                       ArrayList<String> problemDescriptions) throws SQLException;
    void getTeams(String title, String location, ArrayList<String> teamNames) throws SQLException;
    void getDocuments(String teamName, String hackTitle, String location,
                      ArrayList<String> docTitles, ArrayList<String> contents, ArrayList<String> comments) throws SQLException;
}