package model;

public class Utente {
    private String username;
    private String password;

    private signUpHackathon() {

    }

    private createTeam(String nomeTeam) {
        Team t = new Team (nomeTeam, this);
    }

    private joinTeam(Team team) {
        team.listaPartecipanti.add(this);
    }

    private Utente(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
