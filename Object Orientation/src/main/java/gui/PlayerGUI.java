package gui;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import controller.*;

public class PlayerGUI {
    private JFrame frame;
    private JPanel mainPanel;
    private ButtonGroup teamGroup;

    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> locations = new ArrayList<>();
    private ArrayList<String> teamNames = new ArrayList<>();

    public PlayerGUI(Controller controller, JFrame callerFrame) {
        frame = new JFrame("Partecipa a Team");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 245));

        // ====== HEADER ======
        JLabel titleLabel = new JLabel("Seleziona Team a cui Partecipare", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // ====== LISTA TEAM CON RADIOBUTTON ======
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(new Color(240, 240, 245));
        listPanel.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));

        ControllerPlayer controllerPlayer = new ControllerPlayer();
        controllerPlayer.controllerGetHackathons("andrea.romano", titles, locations, teamNames);

        teamGroup = new ButtonGroup();

        int n = teamNames.size();
        for (int i = 0; i < n; i++) {
            JPanel card = createTeamCard(teamNames.get(i), titles.get(i), i);
            listPanel.add(card);
            listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        int cardHeight = 60;
        int gap = 10;
        int totalHeight = n * (cardHeight + gap) + 20;
        listPanel.setPreferredSize(new Dimension(600, Math.max(totalHeight, 300)));

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
        radio.setActionCommand(String.valueOf(index)); // Salviamo l'indice
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
