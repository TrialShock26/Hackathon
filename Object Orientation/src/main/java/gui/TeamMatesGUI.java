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

    /**
     * Crea e visualizza una finestra contenente la lista dei membri di un team,
     * mostrando ogni partecipante all’interno di una card grafica.
     * Include un pulsante per tornare alla schermata precedente.
     *
     * @param controller  il controller principale dell’applicazione
     * @param callerFrame il frame chiamante (schermata precedente)
     * @param teamName    il nome del team
     * @param hackTitle   il titolo dell’hackathon di riferimento
     * @param location    la sede dell’hackathon di riferimento
     */
    public TeamMatesGUI(Controller controller, JFrame callerFrame, String teamName, String hackTitle, String location) {
        this.controller = controller;
        this.teamName = teamName;
        this.hackTitle = hackTitle;
        this.location = location;

        if(!loadTeammates(false)){
            JOptionPane.showMessageDialog(frame,
                    "Errore durante il caricamento dei partecipanti!",
                    "Errore", JOptionPane.ERROR_MESSAGE);
            frame.dispose();
        }

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

        // ====== LABEL NUMERO MEMBRI ======
        JLabel countLabel = new JLabel("Membri totali: " + (names.size() + 1), SwingConstants.CENTER);
        countLabel.setFont(new Font("Arial", Font.BOLD, 16));
        countLabel.setForeground(new Color(60, 100, 170));
        countLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        countLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 10, 0));
        headerPanel.add(countLabel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // ====== LISTA PARTECIPANTI ======
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(new Color(240, 240, 245));
        listPanel.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));

        JPanel card_myself = createParticipantCard(
                controller.getUser().getName() + " " + controller.getUser().getSurname() + " (Tu)"
        );

        for (int i = 0; i < names.size(); i++) {
            JPanel card = createParticipantCard(names.get(i) + " " + surnames.get(i));
            listPanel.add(card);
            listPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        listPanel.add(card_myself);

        // Calcola altezza dinamica
        int cardHeight = 50;
        int gap = 8;
        int totalHeight = names.size() * (cardHeight + gap) + 20;
        listPanel.setPreferredSize(new Dimension(500, Math.max(totalHeight, 300)));

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ====== PANEL INFERIORE ======
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(240, 240, 245));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Bottone "Indietro" (sinistra)
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

        // Bottone "Aggiorna" (destra)
        JButton refreshBtn = new JButton("Aggiorna");
        refreshBtn.setPreferredSize(new Dimension(120, 35));
        refreshBtn.setBackground(new Color(70, 130, 180));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.addActionListener(e -> {
            refreshTeammates();
        });

        // Pannelli separati per allineamento sinistra e destra
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

    private void refreshTeammates() {
        names.clear();
        surnames.clear();

        //  Ricarica i dati dal controller
        loadTeammates(true);

        // Aggiorna la GUI
        populateTeammates();
    }

    private void populateTeammates() {
        // Pannello principale della lista
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(new Color(240, 240, 245));
        listPanel.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));

        // Ricrea la card per te stesso
        JPanel card_myself = createParticipantCard(
                controller.getUser().getName() + " " + controller.getUser().getSurname() + " (Tu)"
        );

        // Aggiungi i teammates
        for (int i = 0; i < names.size(); i++) {
            JPanel card = createParticipantCard(names.get(i) + " " + surnames.get(i));
            listPanel.add(card);
            listPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        // Aggiungi la card dell'utente
        listPanel.add(card_myself);

        // Calcola altezza dinamica
        int cardHeight = 50;
        int gap = 8;
        int totalHeight = names.size() * (cardHeight + gap) + 20;
        listPanel.setPreferredSize(new Dimension(500, Math.max(totalHeight, 300)));

        // Aggiorna lo JScrollPane esistente
        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        frame.getContentPane().remove(1); // Rimuove il vecchio scrollPane (assumendo sia il secondo componente in BorderLayout.CENTER)
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Rinfresca la GUI
        frame.revalidate();
        frame.repaint();
    }

    private boolean loadTeammates(boolean refreshing){
        boolean isCorrect = true;

        // ====== RECUPERA I PARTECIPANTI ======
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

            if(names.isEmpty()){
                isCorrect = false;
            }

        } catch (SQLException e) {
            String error = e.getMessage();
            int idx = error.indexOf("\n");
            error = error.substring(0, idx);
            JOptionPane.showMessageDialog(frame, "C'è stato un errore!\n" + error, "Errore", JOptionPane.ERROR_MESSAGE);
            isCorrect = false;
        }

        return isCorrect;
    }

    /**
     * Crea una card che rappresenta graficamente un partecipante del team.
     * Ogni card contiene il nome e il cognome del partecipante,
     * e viene inserita all’interno della lista visualizzata nella GUI.
     *
     * @param participantName il nome completo del partecipante da visualizzare
     * @return il pannello JPanel contenente le informazioni del partecipante
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