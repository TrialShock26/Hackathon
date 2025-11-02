package gui;

import java.awt.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.*;
import controller.*;
import dao.PlannerDAO;
import model.Hackathon;

public class PlannerGUI {
    private JFrame frame;
    private JPanel mainPanel;
    private JPanel listPanel;
    private JScrollPane scrollPane;
    private ButtonGroup hackathonGroup;

    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> locations = new ArrayList<>();
    private ArrayList<Long> periodOftime = new ArrayList<>();
    private ArrayList<String> problemDescriptions = new ArrayList<>();
    private ArrayList<Date> startDate = new ArrayList<>();
    private ArrayList<Date> endDate = new ArrayList<>();
    private ArrayList<Date> startSubDate = new ArrayList<>();
    private ArrayList<Date> endSubDate = new ArrayList<>();
    private ArrayList<Integer> maxPlayers = new ArrayList<>();
    private ArrayList<Integer> maxTeamDim = new ArrayList<>();

    private Controller controller;

    public PlannerGUI(Controller controller, JFrame callerFrame) {

        this.controller = controller;

        frame = new JFrame("Gestisci");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 245));

        // ====== HEADER ======
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 245));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel titleLabel = new JLabel("Gestisci Hackathon", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton refreshBtn = new JButton("Aggiorna");
        refreshBtn.setPreferredSize(new Dimension(120, 35));
        refreshBtn.setBackground(new Color(70, 130, 180));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.addActionListener(e -> {
            refreshHackathons();
        });

        headerPanel.add(refreshBtn, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // ====== LISTA HACKATHON CON RADIOBUTTON ======
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

        populateHackathonList();

        // ====== PANEL INFERIORE ======
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(240, 240, 245));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20));

        // Bottone "Indietro" a sinistra
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

        // Bottone "Apri" al centro
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setBackground(new Color(240, 240, 245));

        JButton openBtn = new JButton("Apri");
        openBtn.setPreferredSize(new Dimension(120, 35));
        openBtn.setBackground(new Color(70, 130, 180));
        openBtn.setForeground(Color.WHITE);
        openBtn.setFocusPainted(false);
        openBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        openBtn.addActionListener(e -> {
            String selectedHackathon = getSelectedHackathon();
            if (selectedHackathon == null) {
                JOptionPane.showMessageDialog(frame, "Seleziona un hackathon!", "Errore", JOptionPane.ERROR_MESSAGE);
            } else {
                frame.setVisible(false);
                new ResumeGUI(controller, frame, selectedHackathon);
            }
        });

        centerPanel.add(openBtn);
        bottomPanel.add(centerPanel, BorderLayout.CENTER);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);

        if(!loadHackathons( false)) {
            JOptionPane.showMessageDialog(null,
                    "Errore: Non sei un Organizzatore!",
                    "Errore", JOptionPane.ERROR_MESSAGE);
            callerFrame.setVisible(true);
            frame.dispose();
        }
    }

    // ====== RICARICA I DATI DAL DB ======
    private boolean loadHackathons(boolean refreshing) {

        boolean isCorrect = true;

        try {
            controller.getControllerPlanner().controllerGetHackathons(
                    controller.getUser().getUsername(), titles, locations, periodOftime,
                    problemDescriptions, startDate, endDate, startSubDate, endSubDate,
                    maxPlayers, maxTeamDim,refreshing);

            if (titles.isEmpty()) {
                isCorrect = false;
            }

        } catch (SQLException e) {
            String error = e.getMessage();
            if (error.indexOf("\n") > 0) {
                error = error.substring(0, error.indexOf("\n"));
            }
            JOptionPane.showMessageDialog(null,
                    "C'è stato un errore!\n" + error,
                    "Errore", JOptionPane.ERROR_MESSAGE);
            isCorrect = false;
        }

        return isCorrect;
    }

    // ====== RICREA COMPLETAMENTE LA LISTA ======
    private void refreshHackathons() {

        // svuotamento di tutte le informazioni relative agli hackathon
        titles.clear();
        locations.clear();
        periodOftime.clear();
        problemDescriptions.clear();
        startDate.clear();
        endDate.clear();
        startSubDate.clear();
        endSubDate.clear();
        maxPlayers.clear();
        maxTeamDim.clear();

        // 1. Ricarica i dati dal DB
        loadHackathons(true);

        // 2. Rimuovi TUTTI i componenti dalla lista
        listPanel.removeAll();

        // 3. Ricrea il ButtonGroup da zero
        hackathonGroup = new ButtonGroup();

        // 4. Ricostruisci tutte le card con i nuovi dati
        for (int i = 0; i < titles.size(); i++) {
            JPanel card = createHackathonCard(titles.get(i));
            listPanel.add(card);
            listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        // ridisegno della GUI
        listPanel.revalidate();
        listPanel.repaint();
    }

    // ====== POPOLA LA LISTA HACKATHON (SOLO PER IL COSTRUTTORE) ======
    private void populateHackathonList() {
        hackathonGroup = new ButtonGroup();

        for (String hackathon : titles) {
            JPanel card = createHackathonCard(hackathon);
            listPanel.add(card);
            listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
    }

    // ====== CREAZIONE CARD HACKATHON ======
    private JPanel createHackathonCard(String hackathonName) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JRadioButton radio = new JRadioButton(hackathonName);
        radio.setBackground(Color.WHITE);
        radio.setFont(new Font("Arial", Font.PLAIN, 16));
        radio.setCursor(new Cursor(Cursor.HAND_CURSOR));
        hackathonGroup.add(radio);

        card.add(radio, BorderLayout.CENTER);

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

    // ====== OTTIENI HACKATHON SELEZIONATO ======
    private String getSelectedHackathon() {
        for (Enumeration<AbstractButton> buttons = hackathonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) return button.getText();
        }
        return null;
    }
}