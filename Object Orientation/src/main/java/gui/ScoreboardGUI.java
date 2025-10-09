package gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import controller.Controller;

public class ScoreboardGUI {
    private JFrame frame;
    private JPanel mainPanel;

    // Classe interna per i dati dei team
    private static class TeamScore {
        String teamName;
        int score;

        public TeamScore(String teamName, int score) {
            this.teamName = teamName;
            this.score = score;
        }
    }

    public ScoreboardGUI(Controller controller, JFrame callerFrame, String hackathonName) {
        frame = new JFrame("Classifica - " + hackathonName);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 245));

        // ===== HEADER =====
        JLabel titleLabel = new JLabel("Classifica Hackathon: " + hackathonName, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // ===== LISTA TEAM ORDINATA =====
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(new Color(240, 240, 245));
        listPanel.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));

        // Esempio di dati (da sostituire con dati reali da controller)
        List<TeamScore> teams = new ArrayList<>();
        teams.add(new TeamScore("Team Alpha", 10));
        teams.add(new TeamScore("Team Beta", 8));
        teams.add(new TeamScore("Team Gamma", 6));
        teams.add(new TeamScore("Team Delta", 6));
        teams.add(new TeamScore("Team Epsilon", 5));

        // Ordina in ordine decrescente
        teams.sort(Comparator.comparingInt((TeamScore t) -> t.score).reversed());

        int rank = 1;
        for (TeamScore t : teams) {
            JPanel card = createTeamCard(t, rank);
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

        // ===== PANNELLO INFERIORE =====
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

    // ===== CREAZIONE CARD TEAM =====
    private JPanel createTeamCard(TeamScore team, int rank) {
        JPanel card = new JPanel(new BorderLayout());
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        // Colore podio
        Color bgColor = Color.WHITE;
        switch (rank) {
            case 1: bgColor = new Color(255, 215, 0); break; // oro
            case 2: bgColor = new Color(192, 192, 192); break; // argento
            case 3: bgColor = new Color(205, 127, 50); break; // bronzo
        }
        card.setBackground(bgColor);

        // Label team
        JLabel teamLabel = new JLabel(rank + ". " + team.teamName);
        teamLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        card.add(teamLabel, BorderLayout.WEST);

        // Label voto
        JLabel scoreLabel = new JLabel(String.valueOf(team.score));
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        card.add(scoreLabel, BorderLayout.EAST);

        return card;
    }
}
