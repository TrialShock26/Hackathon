package dao;

public interface PlayerDAO {
    void subscribe(String username, String title, String location);

    void joinTeam(String username, String teamName, String hackathonName);
}