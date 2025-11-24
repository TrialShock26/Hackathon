package gui;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.*;
import controller.*;

/**
 * Interfaccia grafica che mostra i compagni di squadra di un partecipante
 * a un hackathon.
 * Questa GUI permette di visualizzare l’elenco dei partecipanti di un team
 * (incluso l’utente stesso) in una finestra scorrevole, con la possibilità di tornare
 * alla schermata chiamante.
 */
public class TeamMatesGUI {
    private JFrame frame;
    private Controller controller;
    private String teamName;
    private String hackTitle;
    private String location;
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> surnames = new ArrayList<>();

    private JPanel listPanel;
    private JScrollPane scrollPane;
    private JLabel countLabel;

    /**
     * Crea e visualizza una finestra contenente la lista dei membri di un team,
     * mostrando ogni partecipante all’interno di una card grafica.
     * Include un pulsante per tornare alla schermata precedente.
     *
     * @param controller        il controller principale
     * @param callerFrame       il frame chiamante a cui ritornare dopo l'uscita
     * @param teamName          il nome del team di riferimento
     * @param hackTitle         il titolo dell'hackathon di riferimento
     * @param location          la sede dell'hackathon di riferimento
     */
    public TeamMatesGUI(Controller controller, JFrame callerFrame, String teamName, String hackTitle, String location) {
        this.controller = controller;
        this.teamName = teamName;
        this.hackTitle = hackTitle;
        this.location = location;

        frame = new JFrame("Partecipanti del Team");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 245));

        // ====== HEADER ======
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(240, 240, 245));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        JLabel titleLabel = new JLabel("Partecipanti - " + teamName, SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerPanel.add(titleLabel);

        loadTeammates(false);

        // ====== LABEL NUMERO MEMBRI ======
        countLabel = new JLabel("Membri totali: " + names.size(), SwingConstants.CENTER);
        countLabel.setFont(new Font("Arial", Font.BOLD, 16));
        countLabel.setForeground(new Color(60, 100, 170));
        countLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        countLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 10, 0));
        headerPanel.add(countLabel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // ====== LISTA PARTECIPANTI ======
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(new Color(240, 240, 245));
        listPanel.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));

        populateTeammates();

        scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ====== PANEL INFERIORE ======
        JPanel bottomPanel = new JPanel(new BorderLayout());
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
            callerFrame.setVisible(true);
        });

        JButton refreshBtn = new JButton("Aggiorna");
        refreshBtn.setPreferredSize(new Dimension(120, 35));
        refreshBtn.setBackground(new Color(70, 130, 180));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.addActionListener(e -> refreshTeammates());

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setBackground(new Color(240, 240, 245));
        leftPanel.add(backBtn);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setBackground(new Color(240, 240, 245));
        rightPanel.add(refreshBtn);

        bottomPanel.add(leftPanel, BorderLayout.WEST);
        bottomPanel.add(rightPanel, BorderLayout.EAST);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    /**
     * Aggiorna la lista dei compagni di squadra,
     * ricostruendo le card.
     */
    private void refreshTeammates() {
        names.clear();
        surnames.clear();

        loadTeammates(true);
        populateTeammates();

        countLabel.setText("Membri totali: " + names.size());
    }

    /**
     * Popola la lista dei compagni di squadra nella GUI.
     */
    private void populateTeammates() {
        listPanel.removeAll();

        for (int i = 0; i < names.size(); i++) {
            if(names.get(i).equals(controller.getPlayer().getName())) {
                JPanel card = createParticipantCard(names.get(i) + " " + surnames.get(i) + " " + "(Tu)");
                listPanel.add(card);
                listPanel.add(Box.createRigidArea(new Dimension(0, 8)));

            } else {
                JPanel card = createParticipantCard(names.get(i) + " " + surnames.get(i));
                listPanel.add(card);
                listPanel.add(Box.createRigidArea(new Dimension(0, 8)));
            }
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    /**
     * Carica i nomi e cognomi dei membri del team per mostrarli
     * all'utente.
     *
     * @param refreshing true se si vuole forzare l'aggiornamento da database
     * @return true se sono stati caricati con successo dei dati, false altrimenti
     */
    private boolean loadTeammates(boolean refreshing) {
        boolean isCorrect = true;

        try {
            controller.getControllerPlayer().controllerGetTeammates(
                    controller.getUser().getUsername(),
                    teamName,
                    hackTitle,
                    location,
                    names,
                    surnames,
                    refreshing
            );

            if (names.isEmpty()) {
                isCorrect = false;
            }

        } catch (SQLException e) {
            String error = e.getMessage();
            int idx = error.indexOf("\n");
            if (idx > 0) error = error.substring(0, idx);
            JOptionPane.showMessageDialog(frame, "C'è stato un errore!\n" + error, "Errore", JOptionPane.ERROR_MESSAGE);
            isCorrect = false;
        }

        return isCorrect;
    }

    /**
     * Crea una card grafica per un partecipante.
     *
     * @param participantName il nome del partecipante da inserire nella card
     * @return un {@link JPanel} che rappresenta la card creata
     */
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
