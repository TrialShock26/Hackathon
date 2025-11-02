package dao;

import java.sql.SQLException;
import java.util.ArrayList;

public interface TeamDAO {
    void publishProgress(String teamName, String hackTitle, String location, String docTitle, String content) throws SQLException;
    void getDocuments(String teamName, String hackTitle, String location,
                      ArrayList<String> docTitles, ArrayList<String> contents, ArrayList<String> comments) throws SQLException;
}