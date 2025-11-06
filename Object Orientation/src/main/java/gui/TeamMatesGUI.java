package gui;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.*;
import controller.*;

/**
 * The type Team mates gui.
 */
public class TeamMatesGUI {
    private JFrame frame;

    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> surnames = new ArrayList<>();

    /**
     * Instantiates a new Team mates gui.
     *
     * @param controller  the controller
     * @param callerFrame the caller frame
     * @param teamName    the team name
     * @param hackTitle   the hack title
     * @param location    the location
     */
    public TeamMatesGUI(Controller controller, JFrame callerFrame, String teamName, String hackTitle, String location) {
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

        // ====== RECUPERA I PARTECIPANTI ======
        try{
            controller.getControllerPlayer().controllerGetTeammates(controller.getUser().getUsername(),
                    teamName, hackTitle, location, names, surnames);
        } catch (SQLException e){
            String error = e.getMessage();
            int idx = error.indexOf("\n");
            error = error.substring(0, idx);
            JOptionPane.showMessageDialog(frame,
                    "C'è stato un errore!\n" + error,
                    "Errore", JOptionPane.ERROR_MESSAGE);
        }

        // ====== LABEL NUMERO MEMBRI ======
        //numero di nomi = di partecipanti + 1 (myself)
        JLabel countLabel = new JLabel("Membri totali: " + (names.size()+1), SwingConstants.CENTER);
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

        JPanel card_myself = createParticipantCard(controller.getUser().getName() + " " +
                controller.getUser().getSurname() + " (Tu)");

        for (int i = 0;i < names.size();i++) {
            JPanel card = createParticipantCard(names.get(i) + " " + surnames.get(i));
            listPanel.add(card);
            listPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        //aggiunta dell'utente stesso tra i partecipanti del team
        //solo a livello di presentazione, non è stato quindi aggiunto all'insieme di partecipanti nel controller

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
            callerFrame.setVisible(true);
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
