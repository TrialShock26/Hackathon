package gui;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.*;
import controller.*;

/**
 * Interfaccia grafica dedicata ai partecipanti.
 * Permette a un utente giocatore di visualizzare i team a cui partecipa, di selezionarne uno
 * per aprirne i dettagli, oppure di cambiare squadra.
 */
public class PlayerGUI {
    private JFrame frame;
    private JPanel mainPanel;
    private JPanel listPanel;
    private JScrollPane scrollPane;
    private ButtonGroup teamGroup;

    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> locations = new ArrayList<>();
    private ArrayList<String> teamNames = new ArrayList<>();

    private Controller controller;

    /**
     * Inizializza l'interfaccia grafica per il giocatore, caricando le informazioni
     * sui team a cui partecipa e fornendo pulsanti per aggiornare i risultati o cambiare team.
     *
     * @param controller  il controller principale dell'applicazione
     * @param callerFrame il frame chiamante, da riattivare quando si torna indietro
     */
    public PlayerGUI(Controller controller, JFrame callerFrame) {

        this.controller =  controller;

        frame = new JFrame("Gioca");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 245));

        // ====== HEADER ======
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 245));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("Seleziona Team", SwingConstants.LEFT);
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

        if(!loadHackathons(false)){
            JOptionPane.showMessageDialog(null,
                    "Errore: Non Partecipi ad alcun Team!",
                    "Errore", JOptionPane.ERROR_MESSAGE);
            callerFrame.setVisible(true);
            frame.dispose();
        }

        populateTeamList();

    }

    /**
     * Carica la lista dei team da mostrare all'utente.
     *
     * @param refreshing indica se il caricamento è un aggiornamento forzato dal database (true)
     *                   o un caricamento regolare (false)
     * @return true se il caricamento è avvenuto correttamente, false in caso di errore
     */
    private boolean loadHackathons(boolean refreshing) {

        boolean isCorrect = true;

        try {
            controller.getControllerPlayer().controllerGetHackathons(
                    controller.getUser().getUsername(), titles, locations, teamNames,refreshing);

            if (teamNames.isEmpty()) {isCorrect = false;}

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
     * Aggiorna completamente le liste dei dati dei team, ricaricando i dati
     * e ricostruendo i componenti grafici nella finestra.
     */
    private void refreshHackathons() {

        //svuotamento di tutte le informazioni relative agli hackathon e ai team
        titles.clear();
        locations.clear();
        teamNames.clear();

        // 1. Ricarica i dati dal DB
        loadHackathons(true);

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

        // ridisegno della GUI
        listPanel.revalidate();
        listPanel.repaint();

    }

    /**
     * Popola la lista dei team alla prima apertura della GUI con
     * le card per i team.
     */
    private void populateTeamList() {
        teamGroup = new ButtonGroup();

        for (int i = 0; i < teamNames.size(); i++) {
            JPanel card = createTeamCard(teamNames.get(i), titles.get(i), i);
            listPanel.add(card);
            listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
    }

    /**
     * Crea una card per rappresentare un team.
     * Ogni elemento contiene il nome del team, il titolo e la sede
     * dell'hackathon di riferimento e un pulsante {@link JRadioButton} per la selezione.
     *
     * @param teamName       il nome del team
     * @param hackathonName  il nome dell'hackathon associato
     * @param index          l'indice del team nella lista
     * @return un oggetto {@link JPanel} contenente le informazioni del team
     */
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
     * Restituisce l'indice del team selezionato dall'utente.
     *
     * @return l'indice del team selezionato, oppure -1 se nessun team è selezionato
     */
    private int getSelectedTeamIndex() {
        ButtonModel selectedModel = teamGroup.getSelection();
        return (selectedModel != null) ? Integer.parseInt(selectedModel.getActionCommand()) : -1;
    }
}