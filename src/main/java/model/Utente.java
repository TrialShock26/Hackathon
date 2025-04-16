package model;

public class Utente {
    private String username;
    private String password;

    private void signUpHackathon() {

    }

    private Team createTeam(String nomeTeam) {
        Team t = new Team (nomeTeam, this);
        return t;
    }

    private void joinTeam(Team team) {
        team.listaPartecipanti.add(this);
    }

    public Utente() {

    }

    public Utente(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
