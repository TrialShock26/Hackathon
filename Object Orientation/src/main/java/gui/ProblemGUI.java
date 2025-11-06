package gui;

import java.awt.*;
import java.sql.SQLException;
import javax.swing.*;
import controller.*;

/**
 * The type Problem gui.
 */
public class ProblemGUI {
    private JFrame frame;
    private String oldDescription;
    private String newDescription;

    /**
     * Instantiates a new Problem gui.
     *
     * @param controller         the controller
     * @param callerFrame        the caller frame
     * @param hackathonName      the hackathon name
     * @param location           the location
     * @param problemDescription the problem description
     */
    public ProblemGUI(Controller controller, JFrame callerFrame, String hackathonName, String location, String problemDescription) {
        frame = new JFrame("Problema - " + hackathonName);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(900, 700);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 245));

        // ====== HEADER ======
        JLabel titleLabel = new JLabel("Problema Hackathon", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // ====== PANEL CENTRALE CON DESCRIZIONE PROBLEMA ======
        JPanel problemPanel = new JPanel(new BorderLayout());
        problemPanel.setBackground(Color.WHITE);
        problemPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        // Titolo del problema
        JLabel problemTitle = new JLabel("Problema: " + hackathonName);
        problemTitle.setFont(new Font("Arial", Font.BOLD, 22));
        problemTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        problemPanel.add(problemTitle, BorderLayout.NORTH);

        // Area di testo per la descrizione del problema
        JTextArea problemDescriptionArea = new JTextArea();
        problemDescriptionArea.setFont(new Font("Arial", Font.PLAIN, 16));
        problemDescriptionArea.setLineWrap(true);
        problemDescriptionArea.setWrapStyleWord(true);
        problemDescriptionArea.setEditable(true);
        problemDescriptionArea.setBackground(Color.WHITE);
        oldDescription = problemDescription;
        problemDescriptionArea.setText(oldDescription);

        JScrollPane scrollPane = new JScrollPane(problemDescriptionArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        problemPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(problemPanel, BorderLayout.CENTER);

        // ====== PANEL INFERIORE CON BOTTONE INDIETRO ======
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

        JButton publishBtn = new JButton("Pubblica");
        publishBtn.setPreferredSize(new Dimension(120, 35));
        publishBtn.setBackground(new Color(70, 130, 180));
        publishBtn.setForeground(Color.WHITE);
        publishBtn.setFocusPainted(false);
        publishBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        publishBtn.addActionListener(e -> {
            newDescription = problemDescriptionArea.getText().trim();
            if (problemDescription.equals("Descrizione assente.")) {
                try {
                    controller.getControllerJudge().controllerPublishProblem(hackathonName, location, newDescription);
                } catch (SQLException | IllegalAccessException ex) {
                    String error = ex.getMessage();
                    int idx = error.indexOf("\n");
                    error = error.substring(0, idx);
                    JOptionPane.showMessageDialog(frame,
                            "C'è stato un errore!\n" + error,
                            "Errore", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Non è più possibile cambiare la descrizione",
                        "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        bottomPanel.add(publishBtn);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }
}