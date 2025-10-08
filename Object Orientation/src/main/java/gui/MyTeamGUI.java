package gui;

import java.awt.*;
import javax.swing.*;
import controller.*;

public class MyTeamGUI {
    private JFrame frame;
    private JTextArea textArea;

    public MyTeamGUI(Controller controller, JFrame callerFrame, String teamName) {
        frame = new JFrame("Il Mio Team");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 245));

        // ====== HEADER ======
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 245));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Il Mio Team", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerPanel.add(titleLabel, BorderLayout.NORTH);

        // Pannello informazioni utente e team
        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 10, 5));
        infoPanel.setBackground(new Color(240, 240, 245));

        // Username (potresti prenderlo dal controller)
        String username = "MarioRossi"; // Esempio, sostituisci con dati reali
        JLabel userLabel = new JLabel("Utente: " + username);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel teamLabel = new JLabel("Team: " + teamName);
        teamLabel.setFont(new Font("Arial", Font.BOLD, 18));
        teamLabel.setForeground(new Color(70, 130, 180));

        infoPanel.add(userLabel);
        infoPanel.add(teamLabel);
        headerPanel.add(infoPanel, BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // ====== PANEL CENTRALE CON AREA DI TESTO ======
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel textLabel = new JLabel("Scrivi il contenuto del Documento da Pubblicare:");
        textLabel.setFont(new Font("Arial", Font.BOLD, 16));
        textLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        centerPanel.add(textLabel, BorderLayout.NORTH);

        textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JScrollPane textScrollPane = new JScrollPane(textArea);
        textScrollPane.setPreferredSize(new Dimension(700, 200));
        centerPanel.add(textScrollPane, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // ====== PANEL INFERIORE CON BOTTONI ======
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(240, 240, 245));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

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

        // Pannello destro con i due bottoni
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setBackground(new Color(240, 240, 245));

        // Bottone "Vedi Partecipanti"
        JButton teammatesBtn = new JButton("Vedi Partecipanti");
        teammatesBtn.setPreferredSize(new Dimension(150, 35));
        teammatesBtn.setBackground(new Color(70, 130, 180));
        teammatesBtn.setForeground(Color.WHITE);
        teammatesBtn.setFocusPainted(false);
        teammatesBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        teammatesBtn.addActionListener(e -> {
            frame.setVisible(false);
            new TeamMatesGUI(controller, frame, teamName);
        });

        // Bottone "Pubblica"
        JButton publishBtn = new JButton("Pubblica");
        publishBtn.setPreferredSize(new Dimension(120, 35));
        publishBtn.setBackground(new Color(60, 179, 113)); // Verde
        publishBtn.setForeground(Color.WHITE);
        publishBtn.setFocusPainted(false);
        publishBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        publishBtn.addActionListener(e -> {
            String text = textArea.getText().trim();
            if (text.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Inserisci del testo prima di pubblicare!", "Errore", JOptionPane.WARNING_MESSAGE);
            } else {
                //logica per pubblicazione col db
                JOptionPane.showMessageDialog(frame, "Documento pubblicato con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
                textArea.setText(""); // Pulisce l'area di testo dopo la pubblicazione
            }
        });

        rightPanel.add(publishBtn);
        rightPanel.add(teammatesBtn);
        bottomPanel.add(rightPanel, BorderLayout.EAST);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }
}