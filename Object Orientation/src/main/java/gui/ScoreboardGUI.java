package gui;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import controller.Controller;

/**
 * Interfaccia grafica che rappresenta la schermata della classifica di un hackathon.
 * Permette di visualizzare la classifica di un singolo hackathon o la classifica globale
 * di tutti i team, con funzionalità per aggiornare e ricaricare i dati.
 */
public class ScoreboardGUI {
    private JFrame frame;
    private JPanel mainPanel;
    private JPanel listPanel;
    private JScrollPane scrollPane;
    private final DecimalFormat scoreFormat = new DecimalFormat("#.##");

    private Controller controller;
    private String hackathonName;
    private String location;

    private ArrayList<String> teams = new ArrayList<>();
    private ArrayList<Double> scores = new ArrayList<>();
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> locations = new ArrayList<>();

    /**
     * Crea e visualizza l'interfaccia grafica per la classifica di un hackathon specifico
     * o per la classifica generale di tutti gli hackathon.
     *
     * @param controller    il controller principale
     * @param callerFrame   il frame chiamante
     * @param hackathonName il nome dell'hackathon, {@code null} se nella classifica globale
     * @param location      la sede dell'hackathon, {@code null} se nella classifica globale
     */
    public ScoreboardGUI(Controller controller, JFrame callerFrame, String hackathonName, String location) {
        this.controller = controller;
        this.hackathonName = hackathonName;
        this.location = location;

        // --- PRIMO CARICAMENTO DATI ---
        if(!loadScoreboard(false)) {
            JOptionPane.showMessageDialog(null,
                    "Errore: Non è possibile generare la classifica!",
                    "Errore", JOptionPane.ERROR_MESSAGE);
            callerFrame.setVisible(true);
            return;
        }

        frame = new JFrame(hackathonName != null
                ? "Classifica - " + hackathonName
                : "Classifica Globale");

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(750, 500);
        frame.setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 245));

        // ===== HEADER =====
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 245));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        JLabel titleLabel = new JLabel(
                hackathonName != null
                        ? "Classifica: " + hackathonName
                        : "Classifica Globale",
                SwingConstants.CENTER
        );
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        JButton refreshBtn = new JButton("Aggiorna");
        refreshBtn.setPreferredSize(new Dimension(120, 35));
        refreshBtn.setBackground(new Color(70, 130, 180));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.addActionListener(e -> refreshScoreboard());
        headerPanel.add(refreshBtn, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // ===== LISTA TEAM =====
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(new Color(240, 240, 245));
        listPanel.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));

        scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        populateScoreboard();

        // ===== PANEL INFERIORE =====
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(new Color(240, 240, 245));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20));

        JButton backBtn = new JButton("Indietro");
        backBtn.setPreferredSize(new Dimension(120, 35));
        backBtn.setBackground(new Color(150, 150, 150));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
            frame.dispose();
            callerFrame.setVisible(true);
        });
        bottomPanel.add(backBtn);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    /**
     * Carica le liste dei dati dei team da mostrare all'utente.
     *
     * @param refreshing indica se il caricamento è un aggiornamento forzato dal database (true)
     *                   o un caricamento regolare (false)
     * @return true se il caricamento è avvenuto correttamente, false in caso di errore
     */
    private boolean loadScoreboard(boolean refreshing) {
        boolean isCorrect = true;

        teams.clear();
        scores.clear();
        titles.clear();
        locations.clear();

        try {
            if (hackathonName != null) {
                controller.getControllerHackathon().controllerScoreboard(hackathonName, location, teams, scores, refreshing);
                isCorrect = !teams.isEmpty();
            } else {
                controller.getControllerHackathon().controllerOverallRanking(teams, scores, titles, locations, refreshing);
                isCorrect = !titles.isEmpty();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "C'è stato un errore!\n" + e.getMessage(),
                    "Errore", JOptionPane.ERROR_MESSAGE);
            isCorrect = false;
        }

        return isCorrect;
    }

    /**
     * Aggiorna completamente le liste dei dati dei team presenti,
     * ricaricando i dati e ricostruendo i componenti grafici.
     */
    private void refreshScoreboard() {
        loadScoreboard(true);
        listPanel.removeAll();
        populateScoreboard();
        listPanel.revalidate();
        listPanel.repaint();
    }

    /**
     * Popola la lista dei team con i relativi punteggi, ordinandoli in base al punteggio.
     */
    private void populateScoreboard() {
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < teams.size(); i++) indices.add(i);
        indices.sort(Comparator.comparingDouble(i -> -scores.get(i)));

        int rank = 1;
        for (int i : indices) {
            JPanel card = (hackathonName != null)
                    ? createTeamCard(teams.get(i), scores.get(i), rank)
                    : createGlobalTeamCard(teams.get(i), scores.get(i), titles.get(i), locations.get(i), rank);

            listPanel.add(card);
            listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            rank++;
        }
    }

    /**
     * Crea la card grafica per visualizzare un team in una classifica di un singolo hackathon.
     *
     * @param team  il nome del team
     * @param score il punteggio del team
     * @param rank  la posizione in classifica
     * @return il JPanel rappresentante la card del team
     */
    private JPanel createTeamCard(String team, double score, int rank) {
        JPanel card = new JPanel(new BorderLayout());
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        card.setBackground(getPodiumColor(rank));

        JLabel teamLabel = new JLabel(rank + ". " + team);
        teamLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        card.add(teamLabel, BorderLayout.WEST);

        JLabel scoreLabel = new JLabel(scoreFormat.format(score));
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        card.add(scoreLabel, BorderLayout.EAST);

        return card;
    }

    /**
     * Crea la card grafica per visualizzare un team nella classifica globale.
     *
     * @param team      il nome del team
     * @param score     il punteggio del team
     * @param hackathon il titolo dell'hackathon associato
     * @param location  la sede dell'hackathon associato
     * @param rank      la posizione in classifica
     * @return il JPanel rappresentante la card del team nella classifica globale
     */
    private JPanel createGlobalTeamCard(String team, double score, String hackathon, String location, int rank) {
        JPanel card = new JPanel(new GridLayout(1, 3));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        card.setBackground(getPodiumColor(rank));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(getPodiumColor(rank));
        infoPanel.add(new JLabel("Hackathon: " + hackathon));
        infoPanel.add(new JLabel("Sede: " + location));
        card.add(infoPanel);

        JLabel teamLabel = new JLabel(rank + ". " + team, SwingConstants.CENTER);
        teamLabel.setFont(new Font("Arial", Font.BOLD, 16));
        card.add(teamLabel);

        JLabel scoreLabel = new JLabel(scoreFormat.format(score), SwingConstants.RIGHT);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        card.add(scoreLabel);

        return card;
    }

    /**
     * Restituisce il colore di sfondo per la posizione in classifica (oro, argento, bronzo).
     *
     * @param rank la posizione in classifica
     * @return il colore corrispondente al piazzamento
     */
    private Color getPodiumColor(int rank) {
        return switch (rank) {
            case 1 -> new Color(255, 215, 0);   // oro
            case 2 -> new Color(192, 192, 192); // argento
            case 3 -> new Color(205, 127, 50);  // bronzo
            default -> Color.WHITE;
        };
    }
}