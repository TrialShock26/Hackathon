package dao;

import controller.*;

import java.util.ArrayList;

public interface UserDAO {
    void newUser(String username, String password, String name, String surname);
}