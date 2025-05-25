package gui;

import model.Hackathon;
import model.Player;
import model.Planner;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class Home {
    private JPanel homePanel;
    private JLabel titleLabel;
    private JLabel locationLabel;
    private JLabel dateLabel;
    private JLabel subscriptionLabel;
    private JButton registerButton;
    private JLabel statusLabel;

    private Hackathon hackathon;
    private Player player;

    public Home() {
        initComponents(); // Inizializza la GUI

        // Crea un planner di esempio
        Planner planner = new Planner("planner1", "1234", "Mario", "Rossi");

        // Crea un Hackathon di esempio
        hackathon = new Hackathon(
                "Hack The Future",
                "Milano",
                LocalDate.of(2025, 7, 1),
                LocalDate.of(2025, 7, 3),
                LocalDate.of(2025, 5, 1),
                LocalDate.of(2025, 6, 30),
                100,
                5,
                planner
        );

        // Crea un player di esempio
        player = new Player("user1", "pass1", "Luca", "Bianchi");

        // Popola le etichette con i dati dell'hackathon
        titleLabel.setText("Titolo: " + hackathon.getTitle());
        locationLabel.setText("Luogo: " + hackathon.getLocation());
        dateLabel.setText("Date: " + hackathon.getStartDate() + " - " + hackathon.getEndDate());
        subscriptionLabel.setText("Iscrizioni: " + hackathon.getStartSubscriptionDate() + " - " + hackathon.getEndSubscriptionDate());
        statusLabel.setText("Stato: Non registrato");

        // Aggiungi listener al pulsante di registrazione
        registerButton.addActionListener(e -> {
            int before = hackathon.getRegisteredPlayers().size();
            player.signUpHackathon(hackathon);
            int after = hackathon.getRegisteredPlayers().size();
            if (after > before) {
                statusLabel.setText("Stato: Registrazione avvenuta con successo");
            } else {
                statusLabel.setText("Stato: Registrazione fallita");
            }
        });
    }

    // Metodo per inizializzare tutti i componenti GUI
    private void initComponents() {
        homePanel = new JPanel();
        homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));
        homePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        titleLabel = new JLabel();
        locationLabel = new JLabel();
        dateLabel = new JLabel();
        subscriptionLabel = new JLabel();
        statusLabel = new JLabel();

        registerButton = new JButton("Registrati");

        homePanel.add(titleLabel);
        homePanel.add(locationLabel);
        homePanel.add(dateLabel);
        homePanel.add(subscriptionLabel);
        homePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        homePanel.add(registerButton);
        homePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        homePanel.add(statusLabel);
    }

    // Metodo per restituire il pannello principale
    public JPanel getPanel() {
        return homePanel;
    }

    // Main per avviare la GUI
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Home home = new Home();
            JFrame frame = new JFrame("Home");
            frame.setContentPane(home.getPanel());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setVisible(true);
        });
    }
}
