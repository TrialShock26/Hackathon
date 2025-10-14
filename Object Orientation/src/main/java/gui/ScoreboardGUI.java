package gui;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.IntStream;

import controller.Controller;
import controller.ControllerHackathon;

public class ScoreboardGUI {
    private JFrame frame;
    private JPanel mainPanel;
    private final DecimalFormat scoreFormat = new DecimalFormat("#.#");

    private ArrayList<String> teams = new ArrayList<>();
    private ArrayList<Double> scores = new ArrayList<>();
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> locations = new ArrayList<>();

    public ScoreboardGUI(Controller controller, JFrame callerFrame, String hackathonName, String location) {

        frame = new JFrame(hackathonName != null
                ? "Classifica - " + hackathonName
                : "Classifica Globale");

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

        ControllerHackathon controllerHackathon = new ControllerHackathon();

        if (hackathonName != null) {
            // --- Classifica per singolo hackathon ---
            controllerHackathon.controllerScoreboard(hackathonName, location, teams, scores);
        } else {
            // --- Classifica globale ---
            controllerHackathon.controllerOverallRanking(teams, scores, titles, locations);
        }

        // ===== ORDINAMENTO =====
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

    // ===== CARD per hackathon singolo =====
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

    // ===== CARD per classifica globale =====
    private JPanel createGlobalTeamCard(String team, double score, String hackathon, String location, int rank) {
        JPanel card = new JPanel(new GridLayout(1, 3));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        card.setBackground(getPodiumColor(rank));

        // Info Hackathon
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
