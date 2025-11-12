package gui;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.*;
import controller.Controller;

/**
 * Interfaccia grafica per l'accesso alle
 * classifiche degli hackathon conclusi.
 * Permette all'utente di selezionare un hackathon terminato per visualizzarne la classifica
 * oppure di visualizzare la classifica globale di tutti i team attraverso i vari hackathon.
 */
public class RankingGUI {
    private JFrame frame;
    private JPanel mainPanel;
    private JPanel listPanel;
    private JScrollPane scrollPane;
    private ButtonGroup hackathonGroup;

    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> locations = new ArrayList<>();

    /**
     * Inizializza la finestra grafica per la selezione degli hackathon conclusi e consente
     * di aprire la classifica relativa o quella globale.
     *
     * @param controller  il controller principale dell'applicazione
     * @param callerFrame il frame chiamante da cui viene aperta questa finestra
     */
    public RankingGUI(Controller controller, JFrame callerFrame) {

        // --- PRIMO CARICAMENTO DATI ---
        if(!loadHackathons(controller,false)) {
            JOptionPane.showMessageDialog(null,
                    "Errore: Non ci sono ancora hackathon terminati!",
                    "Errore", JOptionPane.ERROR_MESSAGE);
            callerFrame.setVisible(true);
            return;
        }

        frame = new JFrame("Ranking Hackathon");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 245));

        // ====== HEADER ======
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 245));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel titleLabel = new JLabel("Seleziona Hackathon", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton refreshBtn = new JButton("Aggiorna");
        refreshBtn.setPreferredSize(new Dimension(120, 35));
        refreshBtn.setBackground(new Color(70, 130, 180));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.addActionListener(e -> refreshHackathons(controller));

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

        populateHackathonList();

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

        // PULSANTE GLOBALE a destra
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(new Color(240, 240, 245));
        JButton globalBtn = new JButton("Globale");
        globalBtn.setPreferredSize(new Dimension(150, 40));
        globalBtn.setBackground(new Color(255, 140, 0));
        globalBtn.setForeground(Color.WHITE);
        globalBtn.setFocusPainted(false);
        globalBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        globalBtn.addActionListener(e -> {
            frame.setVisible(false);
            new ScoreboardGUI(controller, frame, null, null);
        });
        rightPanel.add(globalBtn);
        bottomPanel.add(rightPanel, BorderLayout.EAST);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    /**
     * Carica la lista degli hackathon da mostrare all'utente.
     *
     * @param controller il controller principale
     * @param refreshing indica se il caricamento è un aggiornamento forzato dal database (true)
     *                   o un caricamento regolare (false)
     * @return true se il caricamento è avvenuto correttamente, false in caso di errore
     */
    private boolean loadHackathons(Controller controller, boolean refreshing) {
        boolean isCorrect = true;

        try {
            controller.getControllerHackathon().controllerGetClosedHackathons(titles, locations, refreshing);

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
     * Aggiorna completamente le liste dei dati degli hackathon, ricaricando i dati
     * e ricostruendo i componenti grafici nella finestra.
     *
     * @param controller il controller principale
     */
    private void refreshHackathons(Controller controller) {
        titles.clear();
        locations.clear();

        loadHackathons(controller,true);

        listPanel.removeAll();
        hackathonGroup = new ButtonGroup();

        for (String hackathon : titles) {
            JPanel card = createHackathonCard(hackathon);
            listPanel.add(card);
            listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    /**
     * Popola la lista iniziale degli hackathon con i dati caricati al primo avvio della finestra.
     */
    private void populateHackathonList() {
        hackathonGroup = new ButtonGroup();

        for (String hackathon : titles) {
            JPanel card = createHackathonCard(hackathon);
            listPanel.add(card);
            listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
    }

    /**
     * Crea una card rappresentante un singolo hackathon con il relativo pulsante di selezione.
     *
     * @param hackathonName il nome dell’hackathon
     * @return il JPanel corrispondente all’hackathon
     */
    private JPanel createHackathonCard(String hackathonName) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        card.setMaximumSize(new Dimension(700, 80));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        JRadioButton radio = new JRadioButton();
        radio.setBackground(Color.WHITE);
        radio.setActionCommand(hackathonName);
        hackathonGroup.add(radio);

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.add(radio);
        card.add(leftPanel, BorderLayout.WEST);

        JLabel nameLabel = new JLabel(hackathonName);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.add(nameLabel);
        card.add(rightPanel, BorderLayout.CENTER);

        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                radio.setSelected(true);
            }
        });

        return card;
    }

    /**
     * Restituisce il nome dell’hackathon selezionato nella lista.
     *
     * @return il nome dell’hackathon selezionato o {@code null} se nessun hackathon è selezionato
     */
    private String getSelectedHackathon() {
        if (hackathonGroup.getSelection() != null) {
            return hackathonGroup.getSelection().getActionCommand();
        }
        return null;
    }

    /**
     * Restituisce la sede associata all’hackathon selezionato.
     *
     * @return la sede selezionata o {@code null} se nessun hackathon è selezionato
     */
    private String getSelectedLocation() {
        String selected = getSelectedHackathon();
        if (selected == null) return null;
        int index = titles.indexOf(selected);
        if (index >= 0 && index < locations.size()) {
            return locations.get(index);
        }
        return null;
    }
}