package dao;

public interface JudgeDAO {
    void publishProblem(String text, String title, String location);

    void examinateDocument(String username, String docTitle, String content, String teamName, String hackTitle, String location);

    void gradeTeam(String username, String teamName, String title, String location, int value);
}