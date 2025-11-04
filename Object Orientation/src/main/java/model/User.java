package model;

/**
 * Rappresenta un utente generico registrato alla piattaforma.
 * Contiene le informazioni di base dell'utente, come username, password, nome e cognome.
 * Funge da generalizzazione per i vari ruoli presenti sulla piattaforma
 *
 * @see Player
 * @see Planner
 * @see Judge
 */
public class User {
    private String username;
    private String password;
    private String name;
    private String surname;

    /**
     * Costruisce un nuovo utente con i dati forniti.
     *
     * @param username il nome utente utilizzato per l'accesso
     * @param password la password associata all'account
     * @param name     il nome dell'utente
     * @param surname  il cognome dell'utente
     */
    public User(String username, String password, String name, String surname) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
    }

    /**
     * Restituisce il nome utente dell'account.
     *
     * @return lo username dell'utente
     */
    public String getUsername() {return username;}

    /**
     * Restituisce la password dell'account.
     *
     * @return la password dell'utente
     */
    public String getPassword() {return password;}

    /**
     * Restituisce il nome dell'utente.
     *
     * @return il nome dell'utente
     */
    public String getName() {return name;}

    /**
     * Restituisce il cognome dell'utente.
     *
     * @return il cognome dell'utente
     */
    public String getSurname() {return surname;}
}