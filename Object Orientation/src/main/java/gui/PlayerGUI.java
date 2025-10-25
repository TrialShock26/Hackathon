package gui;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.*;
import controller.*;

public class PlayerGUI {
    private JFrame frame;
    private JPanel mainPanel;
    private JPanel listPanel;
    private JScrollPane scrollPane;
    private ButtonGroup teamGroup;

    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> locations = new ArrayList<>();
    private ArrayList<String> teamNames = new ArrayList<>();

    public PlayerGUI(Controller controller, JFrame callerFrame) {

        // --- PRIMO CARICAMENTO DATI ---
        loadHackathons(controller);

        if (teamNames.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Errore: Non sei un Partecipante!",
                    "Errore", JOptionPane.ERROR_MESSAGE);
            callerFrame.setVisible(true);
            return;
        }
        frame = new JFrame("Partecipa a Team");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 245));

        // ====== HEADER ======
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 245));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("Seleziona Team a cui Partecipare", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton refreshBtn = new JButton("Aggiorna");
        refreshBtn.setPreferredSize(new Dimension(120, 35));
        refreshBtn.setBackground(new Color(70, 130, 180));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.addActionListener(e -> {
            refreshHackathons(controller);
        });

        headerPanel.add(refreshBtn, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // ====== LISTA TEAM ======
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

        populateTeamList(controller);

        // ====== PANEL INFERIORE ======
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(240, 240, 245));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20));

        // Bottone "Indietro"
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

        // Pannello centrale con pulsanti "Cambia" e "Apri"
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        centerPanel.setBackground(new Color(240, 240, 245));

        JButton changeBtn = new JButton("Cambia");
        changeBtn.setPreferredSize(new Dimension(120, 35));
        changeBtn.setBackground(new Color(220, 120, 60));
        changeBtn.setForeground(Color.WHITE);
        changeBtn.setFocusPainted(false);
        changeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        changeBtn.addActionListener(e -> {
            int selectedIndex = getSelectedTeamIndex();
            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(frame, "Seleziona un team!", "Errore", JOptionPane.ERROR_MESSAGE);
            } else {
                String selectedTeam = teamNames.get(selectedIndex);
                String selectedTitle = titles.get(selectedIndex);
                String selectedLocation = locations.get(selectedIndex);

                frame.setVisible(false);
                new JoinGUI(controller, frame, selectedTeam, selectedTitle, selectedLocation);
            }
        });

        JButton openBtn = new JButton("Apri");
        openBtn.setPreferredSize(new Dimension(120, 35));
        openBtn.setBackground(new Color(70, 130, 180));
        openBtn.setForeground(Color.WHITE);
        openBtn.setFocusPainted(false);
        openBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        openBtn.addActionListener(e -> {
            int selectedIndex = getSelectedTeamIndex();
            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(frame, "Seleziona un team!", "Errore", JOptionPane.ERROR_MESSAGE);
            } else {
                String selectedTeam = teamNames.get(selectedIndex);
                String selectedTitle = titles.get(selectedIndex);
                String selectedLocation = locations.get(selectedIndex);

                frame.setVisible(false);
                new MyTeamGUI(controller, frame, selectedTeam, selectedTitle, selectedLocation);
            }
        });

        centerPanel.add(changeBtn);
        centerPanel.add(openBtn);
        bottomPanel.add(centerPanel, BorderLayout.CENTER);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    // ====== RICARICA I DATI DAL DB ======
    private void loadHackathons(Controller controller) {
        titles.clear();
        locations.clear();
        teamNames.clear();

        try {
            controller.getControllerPlayer().controllerGetHackathons(
                    controller.getUser().getUsername(), titles, locations, teamNames);

            //  DEBUG - Rimuovi dopo il test
            System.out.println("Dati caricati dal DB:");
            System.out.println("  Team trovati: " + teamNames.size());
            for (int i = 0; i < teamNames.size(); i++) {
                System.out.println("  - Team: " + teamNames.get(i) + " | Hackathon: " + titles.get(i));
            }

        } catch (SQLException e) {
            String error = e.getMessage();
            int idx = error.indexOf("D");
            if (idx > 0) {
                error = error.substring(0, idx);
            }
            JOptionPane.showMessageDialog(null,
                    "C'è stato un errore!\n" + error,
                    "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ====== RICREA COMPLETAMENTE LA LISTA ======
    private void refreshHackathons(Controller controller) {
        System.out.println("\n=== REFRESH INIZIATO ===");

        // 1. Ricarica i dati dal DB
        loadHackathons(controller);

        // 2. Rimuovi TUTTI i componenti dalla lista
        listPanel.removeAll();

        // 3. Ricrea il ButtonGroup da zero
        teamGroup = new ButtonGroup();

        // 4. Ricostruisci tutte le card con i nuovi dati
        for (int i = 0; i < teamNames.size(); i++) {
            JPanel card = createTeamCard(teamNames.get(i), titles.get(i), i);
            listPanel.add(card);
            listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        // 5. Forza il ridisegno della GUI
        listPanel.revalidate();
        listPanel.repaint();

        System.out.println("=== REFRESH COMPLETATO: " + teamNames.size() + " team visualizzati ===\n");
    }

    // ====== POPOLA LA LISTA TEAM (SOLO PER IL COSTRUTTORE) ======
    private void populateTeamList(Controller controller) {
        teamGroup = new ButtonGroup();

        for (int i = 0; i < teamNames.size(); i++) {
            JPanel card = createTeamCard(teamNames.get(i), titles.get(i), i);
            listPanel.add(card);
            listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
    }

    // ====== CREAZIONE CARD TEAM ======
    private JPanel createTeamCard(String teamName, String hackathonName, int index) {
        JPanel card = new JPanel(new BorderLayout());
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        JRadioButton radio = new JRadioButton();
        radio.setBackground(Color.WHITE);
        radio.setCursor(new Cursor(Cursor.HAND_CURSOR));
        radio.setActionCommand(String.valueOf(index));
        teamGroup.add(radio);

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.add(radio);
        card.add(leftPanel, BorderLayout.WEST);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        JLabel teamLabel = new JLabel(teamName);
        teamLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel hackathonLabel = new JLabel("<html><i>" + hackathonName + "</i></html>");
        hackathonLabel.setFont(new Font("Arial", Font.PLAIN, 13));

        infoPanel.add(teamLabel);
        infoPanel.add(hackathonLabel);
        card.add(infoPanel, BorderLayout.CENTER);

        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                radio.setSelected(true);
                radio.requestFocusInWindow();
            }
        });

        return card;
    }
    // ====== OTTIENI INDICE TEAM SELEZIONATO ======
    private int getSelectedTeamIndex() {
        ButtonModel selectedModel = teamGroup.getSelection();
        return (selectedModel != null) ? Integer.parseInt(selectedModel.getActionCommand()) : -1;
    }
}
