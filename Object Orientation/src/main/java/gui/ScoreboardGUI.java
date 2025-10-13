package gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import controller.Controller;
import java.text.DecimalFormat;

public class ScoreboardGUI {
    private JFrame frame;
    private JPanel mainPanel;
    private final DecimalFormat scoreFormat = new DecimalFormat("#.#");

    // ===== Classe interna per i dati dei team =====
    private static class TeamScore {
        String teamName;
        double score;
        String hackathon;
        String location;

        public TeamScore(String teamName, double score) {
            this.teamName = teamName;
            this.score = score;
        }

        public TeamScore(String teamName, double score, String hackathon, String location) {
            this.teamName = teamName;
            this.score = score;
            this.hackathon = hackathon;
            this.location = location;
        }
    }

    public ScoreboardGUI(Controller controller, JFrame callerFrame, String hackathonName) {

        frame = new JFrame(hackathonName != null ?
                "Classifica - " + hackathonName :
                "Classifica Globale");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(750, 500);
        frame.setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 245));

        // ===== HEADER =====
        JLabel titleLabel = new JLabel(
                hackathonName != null
                        ? "Classifica Hackathon: " + hackathonName
                        : "Classifica Globale",
                SwingConstants.CENTER
        );
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // ===== LISTA TEAM =====
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(new Color(240, 240, 245));
        listPanel.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));

        List<TeamScore> teams = new ArrayList<>();

        if (hackathonName != null) {
            // --- Classifica per singolo hackathon ---
            teams.add(new TeamScore("Team Alpha", 9.8));
            teams.add(new TeamScore("Team Beta", 8.5));
            teams.add(new TeamScore("Team Gamma", 7.6));
            teams.add(new TeamScore("Team Delta", 6.9));
            teams.add(new TeamScore("Team Epsilon", 6.5));
        } else {
            // --- Classifica globale ---
            teams.add(new TeamScore("Team Alpha", 9.8, "Hack4Future", "Milano"));
            teams.add(new TeamScore("Team Beta", 9.2, "TechSprint", "Roma"));
            teams.add(new TeamScore("Team Gamma", 8.7, "Green Hack", "Bologna"));
            teams.add(new TeamScore("Team Delta", 7.9, "AI Challenge", "Napoli"));
            teams.add(new TeamScore("Team Epsilon", 7.4, "Innovathon", "Torino"));
        }

        // Ordina in ordine decrescente
        teams.sort(Comparator.comparingDouble((TeamScore t) -> t.score).reversed());

        int rank = 1;
        for (TeamScore t : teams) {
            JPanel card = (hackathonName != null)
                    ? createTeamCard(t, rank)
                    : createGlobalTeamCard(t, rank);
            listPanel.add(card);
            listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            rank++;
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

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

    // ===== CREAZIONE CARD (hackathon singolo) =====
    private JPanel createTeamCard(TeamScore team, int rank) {
        JPanel card = new JPanel(new BorderLayout());
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        card.setBackground(getPodiumColor(rank));

        JLabel teamLabel = new JLabel(rank + ". " + team.teamName);
        teamLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        card.add(teamLabel, BorderLayout.WEST);

        JLabel scoreLabel = new JLabel(scoreFormat.format(team.score));
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        card.add(scoreLabel, BorderLayout.EAST);

        return card;
    }

    // ===== CREAZIONE CARD (classifica globale) =====
    private JPanel createGlobalTeamCard(TeamScore team, int rank) {
        JPanel card = new JPanel(new GridLayout(1, 3));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        card.setBackground(getPodiumColor(rank));

        // Pannello info hackathon
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(getPodiumColor(rank));
        infoPanel.add(new JLabel("Hackathon: " + team.hackathon));
        infoPanel.add(new JLabel("Sede: " + team.location));
        card.add(infoPanel);

        JLabel teamLabel = new JLabel(rank + ". " + team.teamName, SwingConstants.CENTER);
        teamLabel.setFont(new Font("Arial", Font.BOLD, 16));
        card.add(teamLabel);

        JLabel scoreLabel = new JLabel(scoreFormat.format(team.score), SwingConstants.RIGHT);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        card.add(scoreLabel);

        return card;
    }

    // ===== Colore podio =====
    private Color getPodiumColor(int rank) {
        return switch (rank) {
            case 1 -> new Color(255, 215, 0);   // oro
            case 2 -> new Color(192, 192, 192); // argento
            case 3 -> new Color(205, 127, 50);  // bronzo
            default -> Color.WHITE;
        };
    }
}
