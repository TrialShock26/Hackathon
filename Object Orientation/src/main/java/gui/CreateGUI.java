package gui;

import javax.swing.*;
import java.awt.*;
import controller.Controller;

public class CreateGUI {
    private JFrame frame;
    private JTextField titoloField, sedeField, durataField, dataInizioField, dataFineField,
            periodoIscrizioniField, dataAperturaIscrizioniField, dataChiusuraIscrizioniField,
            maxIscrittiField, maxDimTeamField;

    public CreateGUI(Controller controller, JFrame callerFrame) {
        frame = new JFrame("Crea un Hackathon");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(10, 10)); // <— Layout principale del frame

        // ===== TITOLO CENTRATO IN ALTO =====
        JLabel titleLabel = new JLabel("Crea un Hackathon", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(new Color(50, 50, 50));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        frame.add(titleLabel, BorderLayout.NORTH);

        // ===== PANEL PRINCIPALE CON I CAMPI =====
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int y = 0;

        // Titolo
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Titolo:"), gbc);
        titoloField = new JTextField();
        gbc.gridx = 1; panel.add(titoloField, gbc); y++;

        // Sede
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Sede:"), gbc);
        sedeField = new JTextField();
        gbc.gridx = 1; panel.add(sedeField, gbc); y++;

        // Durata
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Durata:"), gbc);
        durataField = new JTextField();
        gbc.gridx = 1; panel.add(durataField, gbc); y++;

        // Data Inizio
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Data Inizio:"), gbc);
        dataInizioField = new JTextField();
        gbc.gridx = 1; panel.add(dataInizioField, gbc); y++;

        // Data Fine
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Data Fine:"), gbc);
        dataFineField = new JTextField();
        gbc.gridx = 1; panel.add(dataFineField, gbc); y++;

        // Periodo Iscrizioni
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Periodo Iscrizioni:"), gbc);
        periodoIscrizioniField = new JTextField();
        gbc.gridx = 1; panel.add(periodoIscrizioniField, gbc); y++;

        // Data Apertura Iscrizioni
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Data Apertura Iscrizioni:"), gbc);
        dataAperturaIscrizioniField = new JTextField();
        gbc.gridx = 1; panel.add(dataAperturaIscrizioniField, gbc); y++;

        // Data Chiusura Iscrizioni
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Data Chiusura Iscrizioni:"), gbc);
        dataChiusuraIscrizioniField = new JTextField();
        gbc.gridx = 1; panel.add(dataChiusuraIscrizioniField, gbc); y++;

        // Max Iscritti
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Max Iscritti:"), gbc);
        maxIscrittiField = new JTextField();
        gbc.gridx = 1; panel.add(maxIscrittiField, gbc); y++;

        // Max Dimensione Team
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Max Dimensione Team:"), gbc);
        maxDimTeamField = new JTextField();
        gbc.gridx = 1; panel.add(maxDimTeamField, gbc); y++;

        // Giudici (solo pulsante)
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Giudici:"), gbc);

        JButton selezionaButton = new JButton("Seleziona");
        gbc.gridx = 1;
        panel.add(selezionaButton, gbc);
        y++;

        selezionaButton.addActionListener(e -> mostraDialogSelezioneGiudici());

        // ===== PULSANTI FINALI =====
        JPanel bottomPanel = new JPanel();
        JButton salvaButton = new JButton("Salva");
        JButton annullaButton = new JButton("Annulla");
        salvaButton.setBackground(new Color(34, 139, 34));
        salvaButton.setForeground(Color.WHITE);
        annullaButton.setBackground(new Color(150, 150, 150));
        annullaButton.setForeground(Color.WHITE);
        bottomPanel.add(salvaButton);
        bottomPanel.add(annullaButton);

        gbc.gridx = 0; gbc.gridy = y;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(bottomPanel, gbc);

        annullaButton.addActionListener(e -> {
            frame.dispose();
            callerFrame.setVisible(true);
        });

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // Dialog multi-selezione giudici (senza scrollbar)
    private void mostraDialogSelezioneGiudici() {
        String[] utenti = {"Mario Rossi", "Luca Bianchi", "Giulia Verdi", "Sara Neri", "Paolo Gallo"};

        JDialog dialog = new JDialog(frame, "Seleziona Giudici", true);
        dialog.setSize(300, 250);
        dialog.setLocationRelativeTo(frame);
        dialog.setLayout(new BorderLayout(10, 10));

        JLabel infoLabel = new JLabel("Seleziona almeno 2 giudici:");
        infoLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        dialog.add(infoLabel, BorderLayout.NORTH);

        JPanel checkPanel = new JPanel();
        checkPanel.setLayout(new BoxLayout(checkPanel, BoxLayout.Y_AXIS));
        checkPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JCheckBox[] checkBoxes = new JCheckBox[utenti.length];
        for (int i = 0; i < utenti.length; i++) {
            checkBoxes[i] = new JCheckBox(utenti[i]);
            checkPanel.add(checkBoxes[i]);
        }

        dialog.add(checkPanel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();
        JButton confermaBtn = new JButton("Conferma");
        JButton annullaBtn = new JButton("Annulla");
        buttonsPanel.add(confermaBtn);
        buttonsPanel.add(annullaBtn);
        dialog.add(buttonsPanel, BorderLayout.SOUTH);

        confermaBtn.addActionListener(e -> {
            StringBuilder selezionati = new StringBuilder();
            int count = 0;
            for (JCheckBox cb : checkBoxes) {
                if (cb.isSelected()) {
                    selezionati.append(cb.getText()).append("\n");
                    count++;
                }
            }
            if (count < 2) {
                JOptionPane.showMessageDialog(dialog, "Devi selezionare almeno 2 giudici!", "Errore", JOptionPane.ERROR_MESSAGE);
            } else {
                dialog.dispose();
                JOptionPane.showMessageDialog(frame, "Giudici selezionati:\n" + selezionati, "Riepilogo Giudici", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        annullaBtn.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }
}
