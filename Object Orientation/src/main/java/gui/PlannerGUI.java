package gui;

import java.awt.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.*;
import controller.*;

/**
 * Interfaccia grafica per la gestione degli hackathon da parte di un organizzatore.
 * Permette di visualizzare la lista degli hackathon gestiti e accedere
 * ai dettagli di uno specifico hackathon selezionato.
 */
public class PlannerGUI {
    private JFrame frame;
    private JPanel mainPanel;
    private JPanel listPanel;
    private JScrollPane scrollPane;
    private ButtonGroup hackathonGroup;

    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> locations = new ArrayList<>();
    private ArrayList<Long> periodOfTime = new ArrayList<>();
    private ArrayList<String> problemDescriptions = new ArrayList<>();
    private ArrayList<Date> startDate = new ArrayList<>();
    private ArrayList<Date> endDate = new ArrayList<>();
    private ArrayList<Date> startSubDate = new ArrayList<>();
    private ArrayList<Date> endSubDate = new ArrayList<>();
    private ArrayList<Integer> maxPlayers = new ArrayList<>();
    private ArrayList<Integer> maxTeamDim = new ArrayList<>();

    private Controller controller;

    /**
     * Inizializza l'interfaccia grafica per la gestione degli hackathon,
     * costruendo la finestra principale con la lista, i pulsanti e le funzionalità
     * di aggiornamento e selezione.
     *
     * @param controller  il controller principale dell'applicazione
     * @param callerFrame il frame chiamante, da riattivare quando si torna indietro
     */
    public PlannerGUI(Controller controller, JFrame callerFrame) {

        this.controller = controller;

        frame = new JFrame("Gestisci");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
        refreshBtn.addActionListener(e -> refreshHackathons());

        headerPanel.add(refreshBtn, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // ====== LISTA HACKATHON ======
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

        // Bottone "Apri"
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

        if (!loadHackathons(false)) {
            JOptionPane.showMessageDialog(null,
                    "Errore: Non sei un Organizzatore!",
                    "Errore", JOptionPane.ERROR_MESSAGE);
            callerFrame.setVisible(true);
            frame.dispose();
        }

        populateHackathonList();
    }

    /**
     * Carica la lista degli hackathon da mostrare all'utente.
     *
     * @param refreshing indica se il caricamento è un aggiornamento forzato dal database (true)
     *                   o un caricamento regolare (false)
     * @return true se il caricamento è avvenuto correttamente, false in caso di errore
     */
    private boolean loadHackathons(boolean refreshing) {
        boolean isCorrect = true;

        try {
            controller.getControllerPlanner().controllerGetHackathons(
                    controller.getUser().getUsername(), titles, locations, periodOfTime,
                    problemDescriptions, startDate, endDate, startSubDate, endSubDate,
                    maxPlayers, maxTeamDim, refreshing);

            if (titles.isEmpty()) {
                isCorrect = false;
            }

        } catch (SQLException e) {
            String error = e.getMessage();
            int idx = error.indexOf("\n");
            error = error.substring(0, idx);
            JOptionPane.showMessageDialog(frame,
                    "C'è stato un errore!\n" + error,
                    "Errore", JOptionPane.ERROR_MESSAGE);
            isCorrect = false;
        }

        return isCorrect;
    }

    /**
     * Aggiorna completamente le liste dei dati degli hackathon presenti,
     * ricaricando i dati e ricostruendo i componenti grafici.
     */
    private void refreshHackathons() {
        titles.clear();
        locations.clear();
        periodOfTime.clear();
        problemDescriptions.clear();
        startDate.clear();
        endDate.clear();
        startSubDate.clear();
        endSubDate.clear();
        maxPlayers.clear();
        maxTeamDim.clear();

        loadHackathons(true);

        listPanel.removeAll();
        hackathonGroup = new ButtonGroup();

        for (int i = 0; i < titles.size(); i++) {
            JPanel card = createHackathonCard(i);
            listPanel.add(card);
            listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    /**
     * Popola la lista degli hackathon nella GUI alla prima apertura della finestra
     * con le card di visualizzazione.
     */
    // ====== POPOLA LA LISTA HACKATHON ======
    private void populateHackathonList() {
        hackathonGroup = new ButtonGroup();

        for (int i = 0; i < titles.size(); i++) {
            JPanel card = createHackathonCard(i);
            listPanel.add(card);
            listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
    }

    /**
     * Crea una card grafica per rappresentare un singolo hackathon nella lista.
     *
     * @param index l'indice dell'hackathon da visualizzare
     * @return un pannello JPanel contenente il pulsante radio e la struttura grafica della card
     */
    private JPanel createHackathonCard(int index) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Mostra solo il titolo all’utente
        JRadioButton radio = new JRadioButton(titles.get(index));
        radio.setBackground(Color.WHITE);
        radio.setFont(new Font("Arial", Font.PLAIN, 16));
        radio.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Incapsula tutte le info in una stringa concatenata
        String data = String.join("$",
                titles.get(index),
                locations.get(index),
                String.valueOf(periodOfTime.get(index)),
                problemDescriptions.get(index),
                String.valueOf(startDate.get(index)),
                String.valueOf(endDate.get(index)),
                String.valueOf(startSubDate.get(index)),
                String.valueOf(endSubDate.get(index)),
                String.valueOf(maxPlayers.get(index)),
                String.valueOf(maxTeamDim.get(index))
        );

        // L'action command contiene TUTTE le info
        radio.setActionCommand(data);

        hackathonGroup.add(radio);
        card.add(radio, BorderLayout.CENTER);

        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            /**
             * Gestisce l'azione del click su una card
             * che seleziona il relativo hackathon.
             *
             * @param e l'evento che rappresenta il click del mouse
             */
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                radio.setSelected(true);
                radio.requestFocusInWindow();
            }
        });

        return card;
    }

    /**
     * Restituisce il nome dell'hackathon selezionato dall'utente.
     *
     * @return il nome dell'hackathon selezionato, oppure {@code null} se nessuno è stato selezionato
     */
    private String getSelectedHackathon() {
        ButtonModel selected = hackathonGroup.getSelection();
        if (selected == null) return null;
        return selected.getActionCommand();  // Stringa lunga con tutte le info
    }
}