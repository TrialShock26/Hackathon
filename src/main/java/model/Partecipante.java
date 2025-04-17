package model;

public class Partecipante extends Utente{
    public Partecipante(String nome, String cognome) {
        super(nome, cognome);
    }

    private void signUpHackathon() {
    }

    private Team createTeam(String nomeTeam) {
        Team t = new Team (nomeTeam, this);
        return t;
    }

    private void joinTeam(Team team) {
        team.listaPartecipanti.add(this);
    }
}
