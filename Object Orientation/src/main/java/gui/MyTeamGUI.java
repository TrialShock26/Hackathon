package gui;

import java.awt.*;
import java.sql.SQLException;
import javax.swing.*;
import controller.*;

/**
 * The type My team gui.
 */
public class MyTeamGUI {
    private JFrame frame;
    private JTextArea textArea;
    private JTextField titleField;

    /**
     * Instantiates a new My team gui.
     *
     * @param controller  the controller
     * @param callerFrame the caller frame
     * @param teamName    the team name
     * @param hackTitle   the hack title
     * @param location    the location
     */

    public MyTeamGUI(Controller controller, JFrame callerFrame, String teamName, String hackTitle, String location) {
        frame = new JFrame(teamName);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 245));

        // ====== HEADER ======
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 245));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Titolo: mostra il nome reale del team
        JLabel titleLabel = new JLabel(teamName, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(70, 130, 180));
        headerPanel.add(titleLabel, BorderLayout.NORTH);

        // Pannello informazioni utente e team
        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 10, 5));
        infoPanel.setBackground(new Color(240, 240, 245));

        JLabel userLabel = new JLabel("Utente: " + controller.getUser().getName() + " " + controller.getUser().getSurname());
        userLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        infoPanel.add(userLabel);
        headerPanel.add(infoPanel, BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // ====== PANEL CENTRALE CON CAMPO TITOLO E AREA TESTO ======
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Campo titolo
        JLabel titleFieldLabel = new JLabel("Titolo del Documento:");
        titleFieldLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleFieldLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        titleField = new JTextField();
        titleField.setFont(new Font("Arial", Font.PLAIN, 14));
        titleField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        titleField.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        centerPanel.add(titleFieldLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(titleField);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Area testo
        JLabel textLabel = new JLabel("Scrivi il contenuto del Documento da Pubblicare:");
        textLabel.setFont(new Font("Arial", Font.BOLD, 16));
        textLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(textLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JScrollPane textScrollPane = new JScrollPane(textArea);
        textScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        textScrollPane.setPreferredSize(new Dimension(700, 250));
        centerPanel.add(textScrollPane);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // ====== PANEL INFERIORE CON BOTTONI ======
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(240, 240, 245));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

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

        // Bottoni a destra
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setBackground(new Color(240, 240, 245));

        JButton teammatesBtn = new JButton("Vedi Partecipanti");
        teammatesBtn.setPreferredSize(new Dimension(150, 35));
        teammatesBtn.setBackground(new Color(70, 130, 180));
        teammatesBtn.setForeground(Color.WHITE);
        teammatesBtn.setFocusPainted(false);
        teammatesBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        teammatesBtn.addActionListener(e -> {
            frame.setVisible(false);
            new TeamMatesGUI(controller, frame, teamName,hackTitle,location);
        });

        JButton publishBtn = new JButton("Pubblica");
        publishBtn.setPreferredSize(new Dimension(120, 35));
        publishBtn.setBackground(new Color(60, 179, 113)); // Verde
        publishBtn.setForeground(Color.WHITE);
        publishBtn.setFocusPainted(false);
        publishBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        publishBtn.addActionListener(e -> {
            String title = titleField.getText().trim();
            String text = textArea.getText().trim();

            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Inserisci un titolo per il documento!", "Errore", JOptionPane.WARNING_MESSAGE);
            } else if (text.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Inserisci del testo prima di pubblicare!", "Errore", JOptionPane.WARNING_MESSAGE);
            } else {
                try {
                    controller.getControllerTeam().controllerPublishProgress(teamName,hackTitle,location,title,text);
                    JOptionPane.showMessageDialog(frame,
                            "Documento \"" + title + "\" pubblicato con successo!",
                            "Successo", JOptionPane.INFORMATION_MESSAGE);
                    titleField.setText("");
                    textArea.setText("");
                } catch (SQLException ex) {
                    String error = ex.getMessage();
                    int idx = error.indexOf("\n");
                    error = error.substring(0, idx);
                    JOptionPane.showMessageDialog(frame,
                            "C'è stato un errore!\n" + error,
                            "Errore", JOptionPane.ERROR_MESSAGE);
                }
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
