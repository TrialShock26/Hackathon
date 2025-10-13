package dao;

import java.sql.SQLException;
import java.util.ArrayList;

public interface HackathonDAO {
    void overallRanking(ArrayList<String> teamNames, ArrayList<Double> scores, ArrayList<String> titles, ArrayList<String> locations) throws SQLException;
    void scoreboard(String title, String location, ArrayList<String> teamNames, ArrayList<Double> scores) throws SQLException;
}
