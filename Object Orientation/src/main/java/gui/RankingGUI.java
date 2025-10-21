package gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.*;
import controller.Controller;
import controller.ControllerHackathon;

public class RankingGUI {
    private JFrame frame;
    private JPanel mainPanel;
    private ButtonGroup hackathonGroup;

    private ArrayList<String> hackathons = new ArrayList<>();
    private ArrayList<String> locations = new ArrayList<>();

    public RankingGUI(Controller controller, JFrame callerFrame) {
        frame = new JFrame("Ranking Hackathon");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600); // stessa dimensione di PlannerGUI
        frame.setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 245));

        // ====== HEADER ======
        JLabel titleLabel = new JLabel("Seleziona Hackathon", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // ====== LISTA HACKATHON ======
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(new Color(240, 240, 245));
        listPanel.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));

        ControllerHackathon controllerHackathon = new ControllerHackathon();

        controllerHackathon.controllerGetClosedHackathons(hackathons,locations);

        /*
        String[] hackathons = {
                "Hack4Future 2025",
                "TechSprint 2025",
                "Innovathon Roma",
                "AI Challenge",
                "Green Hack 2025",
                "Design Jam 2025",
                "HealthTech Hack"
        };*/

        hackathonGroup = new ButtonGroup();
        for (String hackathon : hackathons) {
            JPanel card = createHackathonCard(hackathon);
            listPanel.add(card);
            listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ====== PANEL INFERIORE ======
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(240, 240, 245));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20));

        // Pulsante Indietro a sinistra
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(new Color(240, 240, 245));
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
        leftPanel.add(backBtn);
        bottomPanel.add(leftPanel, BorderLayout.WEST);

// Pulsante centrale "Apri"
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setBackground(new Color(240, 240, 245));
        JButton viewBtn = new JButton("Apri");
        viewBtn.setPreferredSize(new Dimension(150, 40));
        viewBtn.setBackground(new Color(70, 130, 180));
        viewBtn.setForeground(Color.WHITE);
        viewBtn.setFocusPainted(false);
        viewBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewBtn.addActionListener(e -> {
            String selectedHackathon = getSelectedHackathon();
            String selectedLocation = getSelectedLocation();

            if (selectedHackathon == null) {
                JOptionPane.showMessageDialog(frame, "Seleziona un hackathon!", "Errore", JOptionPane.ERROR_MESSAGE);
            } else {
                frame.setVisible(false);
                new ScoreboardGUI(controller, frame, selectedHackathon, selectedLocation);
            }
        });

        centerPanel.add(viewBtn);
        bottomPanel.add(centerPanel, BorderLayout.CENTER);

// NUOVO PULSANTE GLOBALE a destra
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(new Color(240, 240, 245));
        JButton globalBtn = new JButton("Globale");
        globalBtn.setPreferredSize(new Dimension(150, 40));
        globalBtn.setBackground(new Color(255, 140, 0)); // arancione per evidenza
        globalBtn.setForeground(Color.WHITE);
        globalBtn.setFocusPainted(false);
        globalBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        globalBtn.addActionListener(e -> {
            frame.setVisible(false);
            new ScoreboardGUI(controller, frame, null,null); // sostituisci AnotherGUI con la tua GUI reale
        });
        rightPanel.add(globalBtn);
        bottomPanel.add(rightPanel, BorderLayout.EAST);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);


        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    private JPanel createHackathonCard(String hackathonName) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        // Limita larghezza ma non forza altezza
        card.setMaximumSize(new Dimension(700, 80));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        // RadioButton a sinistra
        JRadioButton radio = new JRadioButton();
        radio.setBackground(Color.WHITE);
        radio.setActionCommand(hackathonName);
        hackathonGroup.add(radio);

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.add(radio);
        card.add(leftPanel, BorderLayout.WEST);

        // Nome hackathon a destra
        JLabel nameLabel = new JLabel(hackathonName);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.add(nameLabel);
        card.add(rightPanel, BorderLayout.CENTER);

        // Rendi cliccabile tutta la card
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                radio.setSelected(true);
            }
        });

        return card;
    }



    private String getSelectedHackathon() {
        if (hackathonGroup.getSelection() != null) {
            return hackathonGroup.getSelection().getActionCommand();
        }
        return null;
    }

    private String getSelectedLocation() {
        String selected = getSelectedHackathon();
        if (selected == null) return null;
        int index = hackathons.indexOf(selected);
        if (index >= 0 && index < locations.size()) {
            return locations.get(index);
        }
        return null;
    }

}
