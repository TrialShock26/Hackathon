package dao;

import controller.*;

import java.util.ArrayList;

public interface UserDAO {
    void newUser(String username, String password, String name, String surname);

    void getUsers(String planUser,
                  ArrayList<String> allUsernames,
                  ArrayList<String> allNames,
                  ArrayList<String> allSurnames,
                  ArrayList<String> allPasswords);
}