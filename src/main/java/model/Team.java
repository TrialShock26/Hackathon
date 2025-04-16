package model;
import java.util.ArrayList;

public class Team {
    private String nome;
    public ArrayList<Utente> listaPartecipanti;

    private void publishProgress (String contenuto) {
        Progresso p = new Progresso(contenuto);
    }

    public Team(String nomeTeam, Utente u) {
        nome = nomeTeam;
        listaPartecipanti = new ArrayList<Utente>();
        listaPartecipanti.add(u);
    }
}
