package gui;

import javax.swing.*;
import java.awt.*;
import controller.*;

public class HubGUI {
    private JFrame frame;
    private JPanel hubPanel;
    private JButton gioButton;
    private JButton creaButton;
    private JButton nuovaPartitaButton;
    private JButton giudicaButton;
    private JButton gestisciButton;
    private JButton classificaButton;
    private JButton indietroButton;
    private JLabel title;
    private JLabel subtitle;

    public HubGUI(Controller controller, JFrame callerFrame) {

        frame = new JFrame("HackathON - HUB");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        hubPanel = new JPanel(new BorderLayout());
        hubPanel.setBackground(new Color(240, 240, 245));

        // --- CENTRO ---
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(240, 240, 245));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        // Titolo principale
        title = new JLabel("Benvenut3 in HackathON!");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(new Color(50, 50, 50));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(title);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Sottotitolo
        subtitle = new JLabel("Che cosa vuoi fare?");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitle.setForeground(new Color(100, 100, 100));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(subtitle);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Pannello dei bottoni (3 righe x 2 colonne)
        JPanel gridButtonsPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        gridButtonsPanel.setBackground(new Color(240, 240, 245));
        gridButtonsPanel.setMaximumSize(new Dimension(400, 220));

        // Crea bottoni più piccoli
        gioButton = createStyledButton("Gioca", new Color(70, 130, 180));
        creaButton = createStyledButton("Crea", new Color(34, 139, 34));
        nuovaPartitaButton = createStyledButton("Nuova Partita", new Color(255, 140, 0));
        giudicaButton = createStyledButton("Valuta", new Color(138, 43, 226));
        gestisciButton = createStyledButton("Gestisci", new Color(220, 20, 60));
        classificaButton = createStyledButton("Classifica", new Color(100, 149, 237));

        // --- Azioni bottoni ---
        creaButton.addActionListener(e -> {
            frame.setVisible(false);
            new CreateGUI(controller, frame);
        });

        gestisciButton.addActionListener(e -> {
            frame.setVisible(false);
            new PlannerGUI(controller, frame);
        });

        giudicaButton.addActionListener(e -> {
            frame.setVisible(false);
            new JudgeGUI(controller, frame);
        });

        gioButton.addActionListener(e -> {
            frame.setVisible(false);
            new PlayerGUI(controller, frame);
        });

        nuovaPartitaButton.addActionListener(e -> {
            frame.setVisible(false);
            new RegistrationGUI(controller, frame);
        });

        classificaButton.addActionListener(e -> {
            frame.setVisible(false);
            new RankingGUI(controller, frame);
        });

        gridButtonsPanel.add(gioButton);
        gridButtonsPanel.add(creaButton);
        gridButtonsPanel.add(nuovaPartitaButton);
        gridButtonsPanel.add(giudicaButton);
        gridButtonsPanel.add(gestisciButton);
        gridButtonsPanel.add(classificaButton);

        centerPanel.add(gridButtonsPanel);
        hubPanel.add(centerPanel, BorderLayout.CENTER);

        // --- BASSO (Indietro + Nome utente) ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(new Color(240, 240, 245));

        indietroButton = new JButton("Indietro");
        indietroButton.setFont(new Font("Arial", Font.BOLD, 13));
        indietroButton.setPreferredSize(new Dimension(100, 30));
        indietroButton.setFocusPainted(false);
        indietroButton.setBackground(new Color(150, 150, 150));
        indietroButton.setForeground(Color.WHITE);
        indietroButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        indietroButton.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(
                    frame,
                    "Vuoi tornare alla schermata precedente?",
                    "Conferma",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (response == JOptionPane.YES_OPTION) {
                frame.dispose();
                new LoginGUI(frame);
            }
        });
        bottomPanel.add(indietroButton);

        // --- Etichetta con nome utente (più evidente) ---
        JLabel userLabel = new JLabel("Ciao, "
                + controller.getUser().getName() + " "
                + controller.getUser().getSurname());
        userLabel.setFont(new Font("Arial", Font.BOLD, 16));
        userLabel.setForeground(new Color(40, 80, 160)); // blu-grigio visibile
        userLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        // Pannello contenitore per posizionamento
        JPanel bottomContainer = new JPanel(new BorderLayout());
        bottomContainer.setBackground(new Color(240, 240, 245));
        bottomContainer.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        bottomContainer.add(bottomPanel, BorderLayout.WEST);
        bottomContainer.add(userLabel, BorderLayout.EAST);

        hubPanel.add(bottomContainer, BorderLayout.SOUTH);

        frame.setContentPane(hubPanel);
        frame.setVisible(true);
    }

    // --- Metodo per creare bottoni uniformi ma più piccoli ---
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(140, 45));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 2),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        return button;
    }
}
