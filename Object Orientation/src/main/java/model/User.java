package model;

/**
 * Classe rappresentante l'utente generico registrato alla piattaforma.
*/
public class User {
    private String username;
    private String password;
    private String name;
    private String surname;

    public User(String username, String password, String name, String surname) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
    }

    public String getUsername() {return username;}
    public String getPassword() {return password;}
    public String getName() {return name;}
    public String getSurname() {return surname;}
}
