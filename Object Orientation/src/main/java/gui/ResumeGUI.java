package gui;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;
import controller.*;

/**
 * Interfaccia grafica che gestisce la visualizzazione del riepilogo di un hackathon.
 * Permette all'organizzatore di visualizzare tutti i dettagli dell’hackathon selezionato,
 * oltre a offrire funzionalità per avviare o terminare l’hackathon
 */
public class ResumeGUI {
    private JFrame frame;
    private JPanel mainPanel;
    private Controller controller;

    /**
     * Inizializza la finestra del riepilogo di un hackathon, mostrando le informazioni
     * principali e fornendo pulsanti per avviare o terminare l’evento.
     *
     * @param controller    il controller principale
     * @param callerFrame   il frame chiamante
     * @param hackathonInfo tutti i dati dell’hackathon di cui visualizzare il riepilogo,
     *                      formattati tramite un carattere separatore
     */

    public ResumeGUI(Controller controller, JFrame callerFrame, String hackathonInfo) {
        this.controller = controller;
        frame = new JFrame("Riepilogo");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 245));

        // ====== HEADER ======
        JLabel titleLabel = new JLabel("Riepilogo Hackathon", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // ====== PANEL CENTRALE CON SCROLL ======
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(240, 240, 245));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));

        ArrayList<String> hackathonInfoFormatted = new ArrayList<>(Arrays.asList(hackathonInfo.split("\\$")));

        // Titolo hackathon
        JLabel hackTitle = new JLabel(hackathonInfoFormatted.getFirst());
        hackTitle.setFont(new Font("Arial", Font.BOLD, 20));
        hackTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(hackTitle);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Creazione di una stringa unica contenente tutte le informazioni di un hackathon,
        // separate dal carattere '$'. L'ordine degli elementi è importante e deve rimanere coerente
        // con l’ordine usato quando verrà effettuato lo split per ricostruire i dati.
        //
        // Indici dei campi (usati poi in hackathonInfoFormatted.get(index)):
        //  0 → Titolo
        //  1 → Location
        //  2 → Durata (periodOfTime)
        //  3 → Descrizione del problema
        //  4 → Data inizio hackathon
        //  5 → Data fine hackathon
        //  6 → Data apertura iscrizioni
        //  7 → Data chiusura iscrizioni
        //  8 → Numero massimo di partecipanti
        //  9 → Dimensione massima dei team
        //

        contentPanel.add(createInfoRow("Sede:", String.valueOf(hackathonInfoFormatted.get(1))));
        contentPanel.add(createInfoRow("Durata:", hackathonInfoFormatted.get(2) + " giorni"));
        contentPanel.add(createInfoRow("Data Inizio:", hackathonInfoFormatted.get(4)));
        contentPanel.add(createInfoRow("Data Fine:", hackathonInfoFormatted.get(5)));
        contentPanel.add(createInfoRow("Data Apertura Iscrizioni:", hackathonInfoFormatted.get(6)));
        contentPanel.add(createInfoRow("Data Chiusura Iscrizioni:", hackathonInfoFormatted.get(7)));
        contentPanel.add(createInfoRow("Max Iscritti:", hackathonInfoFormatted.get(8)));
        contentPanel.add(createInfoRow("Max Dim. Team:", hackathonInfoFormatted.get(9)));

        // ====== DESCRIZIONE PROBLEMA ======
        JLabel problemLabel = new JLabel("Descrizione Problema:");
        problemLabel.setFont(new Font("Arial", Font.BOLD, 14));
        problemLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(problemLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JTextArea problemArea = new JTextArea(hackathonInfoFormatted.get(3));
        problemArea.setFont(new Font("Arial", Font.PLAIN, 14));
        problemArea.setLineWrap(true);
        problemArea.setWrapStyleWord(true);
        problemArea.setEditable(false);
        problemArea.setBackground(new Color(250, 250, 250));
        problemArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JScrollPane problemScroll = new JScrollPane(problemArea);
        problemScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        problemScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        problemScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        problemScroll.setPreferredSize(new Dimension(600, 100));
        problemScroll.setMinimumSize(new Dimension(600, 100));
        problemScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        contentPanel.add(problemScroll);

        // Scroll principale
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ====== PANEL INFERIORE ======
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(240, 240, 245));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20));

        // Bottone "Indietro"
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
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

        // Pulsanti centrali
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        centerPanel.setBackground(new Color(240, 240, 245));

        JButton startBtn = new JButton("Inizia");
        startBtn.setPreferredSize(new Dimension(120, 35));
        startBtn.setBackground(new Color(34, 139, 34));
        startBtn.setForeground(Color.WHITE);
        startBtn.setFocusPainted(false);
        startBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        startBtn.addActionListener(e -> {
            try{
                controller.getControllerPlanner().controllerStartHackathon(hackathonInfoFormatted.getFirst(),
                        hackathonInfoFormatted.get(1)); //title e location
                JOptionPane.showMessageDialog(
                        frame,
                        "Hackathon \"" + hackathonInfoFormatted.getFirst() + "\" avviato!",
                        "Avvio",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } catch (SQLException ex) {
                String error = ex.getMessage();
                int idx = error.indexOf("\n");
                error = error.substring(0, idx);
                JOptionPane.showMessageDialog(frame,
                        "C'è stato un errore!\n" + error,
                        "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton endBtn = new JButton("Termina");
        endBtn.setPreferredSize(new Dimension(120, 35));
        endBtn.setBackground(new Color(220, 20, 60));
        endBtn.setForeground(Color.WHITE);
        endBtn.setFocusPainted(false);
        endBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        endBtn.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(
                    frame,
                    "Vuoi davvero terminare l’hackathon \"" + hackathonInfoFormatted.getFirst() + "\"?",
                    "Conferma terminazione",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (response == JOptionPane.YES_OPTION) {
                try {
                    controller.getControllerPlanner()
                            .controllerEndHackathon(hackathonInfoFormatted.getFirst(), hackathonInfoFormatted.get(1));

                    JDialog dialog = new JDialog(frame, "Hackathon concluso", true);
                    dialog.setSize(420, 200);
                    dialog.setLocationRelativeTo(frame);
                    dialog.setLayout(new BorderLayout());
                    dialog.getContentPane().setBackground(new Color(245, 247, 250));

                    JPanel messagePanel = new JPanel(new BorderLayout());
                    messagePanel.setOpaque(false);
                    messagePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

                    JLabel iconLabel = new JLabel(UIManager.getIcon("OptionPane.informationIcon"));
                    JLabel msgLabel = new JLabel("<html><div style='text-align:center;'>Hackathon <b>\"" + hackathonInfoFormatted.getFirst() + "\"</b><br>terminato con successo!</div></html>");
                    msgLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                    msgLabel.setHorizontalAlignment(SwingConstants.CENTER);

                    messagePanel.add(iconLabel, BorderLayout.WEST);
                    messagePanel.add(msgLabel, BorderLayout.CENTER);
                    dialog.add(messagePanel, BorderLayout.CENTER);

                    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
                    buttonPanel.setOpaque(false);

                    JButton closeBtn = new JButton("Chiudi");
                    closeBtn.setPreferredSize(new Dimension(110, 35));
                    closeBtn.setBackground(new Color(180, 180, 180));
                    closeBtn.setForeground(Color.WHITE);
                    closeBtn.setFocusPainted(false);
                    closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    closeBtn.addActionListener(ev -> dialog.dispose());

                    JButton rankingBtn = new JButton("Classifica");
                    rankingBtn.setPreferredSize(new Dimension(130, 35));
                    rankingBtn.setBackground(new Color(30, 144, 255));
                    rankingBtn.setForeground(Color.WHITE);
                    rankingBtn.setFocusPainted(false);
                    rankingBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    rankingBtn.setFont(new Font("Arial", Font.BOLD, 14));
                    rankingBtn.addActionListener(ev -> {
                        dialog.dispose();
                        frame.dispose();
                        controller.getControllerHackathon().addClosedHackathon(hackathonInfoFormatted.getFirst(), hackathonInfoFormatted.get(1));
                        new ScoreboardGUI(controller, callerFrame, hackathonInfoFormatted.getFirst(), hackathonInfoFormatted.get(1));
                    });

                    buttonPanel.add(closeBtn);
                    buttonPanel.add(rankingBtn);
                    dialog.add(buttonPanel, BorderLayout.SOUTH);
                    dialog.setVisible(true);

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

        centerPanel.add(startBtn);
        centerPanel.add(endBtn);
        bottomPanel.add(centerPanel, BorderLayout.CENTER);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    /**
     * Crea un pannello informativo per una singola coppia etichetta-valore
     * da inserire nella tabella.
     * Ogni riga mostra una proprietà dell’hackathon.
     *
     * @param label l’etichetta descrittiva del campo
     * @param value il valore corrispondente da visualizzare
     * @return un JPanel formattato contenente l’etichetta e il valore
     */
    private JPanel createInfoRow(String label, String value) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setBackground(new Color(240, 240, 245));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel nameLabel = new JLabel(label);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setPreferredSize(new Dimension(200, 25));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        panel.add(nameLabel);
        panel.add(valueLabel);
        return panel;
    }
}