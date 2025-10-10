package dao;

public interface TeamDAO {
    void publishProgress(String teamName, String hackTitle, String location, String docTitle, String content);
}