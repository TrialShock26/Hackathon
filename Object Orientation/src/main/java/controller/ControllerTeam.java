package controller;

import dao.TeamDAO;
import postgresImplementationDao.TeamImplementationDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public class ControllerTeam {

    public void controllerPublishProgress(String teamName, String hackTitle, String location, String docTitle, String content) {
        try{

            TeamDAO teamController = new TeamImplementationDAO();

            teamController.publishProgress(teamName,hackTitle,location,docTitle,content);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }



}
