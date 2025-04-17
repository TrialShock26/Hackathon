package model;
import java.util.ArrayList;

public class Team {
    private String nome;
    public ArrayList<Partecipante> listaPartecipanti  = new ArrayList<Partecipante>();;

    public Team(String nomeTeam, Partecipante u) {
        nome = nomeTeam;
        listaPartecipanti.add(u);
    }

    private void publishProgress (String contenuto) {
        Progresso p = new Progresso(contenuto);
    }

}