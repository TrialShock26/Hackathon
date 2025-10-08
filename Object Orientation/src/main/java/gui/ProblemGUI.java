package gui;

import java.awt.*;
import javax.swing.*;
import controller.*;

public class ProblemGUI {
    private JFrame frame;

    public ProblemGUI(Controller controller, JFrame callerFrame, String hackathonName) {
        frame = new JFrame("Problema - " + hackathonName);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
        JTextArea problemDescription = new JTextArea();
        problemDescription.setFont(new Font("Arial", Font.PLAIN, 16));
        problemDescription.setLineWrap(true);
        problemDescription.setWrapStyleWord(true);
        problemDescription.setEditable(false);
        problemDescription.setBackground(Color.WHITE);

        // Testo di esempio unico per tutti gli hackathon (per testing)
        String descriptionText = "DESCRIZIONE DEL PROBLEMA: " + hackathonName + "\n\n" +
                "CONTESTO:\n" +
                "Questo hackathon mira a risolvere una delle sfide più pressanti del nostro tempo " +
                "attraverso l'innovazione tecnologica e la creatività.\n\n" +
                "PROBLEMA:\n" +
                "Sviluppare una soluzione innovativa che affronti le principali criticità del settore " +
                "utilizzando tecnologie all'avanguardia.\n\n" +
                "REQUISITI TECNICI:\n" +
                "- Architettura scalabile e robusta\n" +
                "- Interfaccia utente intuitiva\n" +
                "- Integrazione con API esistenti\n" +
                "- Documentazione completa del progetto\n\n" +
                "CRITERI DI VALUTAZIONE:\n" +
                "- Innovazione (30%)\n" +
                "- Fattibilità tecnica (25%)\n" +
                "- Impatto potenziale (20%)\n" +
                "- Qualità del codice (15%)\n" +
                "- Presentazione (10%)";

        problemDescription.setText(descriptionText);

        JScrollPane scrollPane = new JScrollPane(problemDescription);
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

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }
}