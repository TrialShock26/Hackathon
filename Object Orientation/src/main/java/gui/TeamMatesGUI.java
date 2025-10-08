package gui;

import java.awt.*;
import javax.swing.*;
import controller.*;

public class TeamMatesGUI {
    private JFrame frame;

    public TeamMatesGUI(Controller controller, JFrame callerFrame, String teamName) {
        frame = new JFrame("Partecipanti del Team");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 245));

        // ====== HEADER ======
        JLabel titleLabel = new JLabel("Partecipanti - " + teamName, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // ====== LISTA PARTECIPANTI ======
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(new Color(240, 240, 245));
        listPanel.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));

        // Lista di partecipanti (esempio, in futuro prendi la lista dal controller)
        String[] participants = {
                "Mario Rossi",
                "Laura Bianchi",
                "Giuseppe Verdi",
                "Anna Neri",
                "Luca Gialli",
                "Sofia Blu"
        };

        for (String participant : participants) {
            JPanel card = createParticipantCard(participant);
            listPanel.add(card);
            listPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        // Forza il preferred size del listPanel in base al numero di elementi
        int cardHeight = 50;
        int gap = 8;
        int totalHeight = participants.length * (cardHeight + gap) + 20;
        listPanel.setPreferredSize(new Dimension(500, Math.max(totalHeight, 300)));

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ====== PANEL INFERIORE ======
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(new Color(240, 240, 245));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JButton backBtn = new JButton("Indietro");
        backBtn.setPreferredSize(new Dimension(120, 35));
        backBtn.setBackground(new Color(150, 150, 150));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
            frame.dispose();
            callerFrame.setVisible(true); // torna a MyTeamGUI
        });

        bottomPanel.add(backBtn);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    // ====== CREAZIONE CARD PARTECIPANTE ======
    private JPanel createParticipantCard(String participantName) {
        JPanel card = new JPanel(new BorderLayout());
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));

        JLabel nameLabel = new JLabel(participantName);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        card.add(nameLabel, BorderLayout.CENTER);

        return card;
    }
}