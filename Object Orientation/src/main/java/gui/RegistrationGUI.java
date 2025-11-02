package gui;

import java.awt.*;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.*;
import controller.*;

public class RegistrationGUI {
    private JFrame frame;
    private JPanel mainPanel;
    private ButtonGroup hackathonGroup;

    public RegistrationGUI(Controller controller, JFrame callerFrame) {
        frame = new JFrame("Registrazione Hackathon");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 245));

        // ====== HEADER ======
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 245));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel titleLabel = new JLabel("Registrati ad un Hackathon", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton refreshBtn = new JButton("Aggiorna");
        refreshBtn.setPreferredSize(new Dimension(120, 35));
        refreshBtn.setBackground(new Color(70, 130, 180));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.addActionListener(e -> {
            // Qui potrai aggiungere la logica per ricaricare la lista
            // (ad esempio refreshHackathons(); se lo implementi)
        });
        headerPanel.add(refreshBtn, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // ====== DATI HACKATHON ======
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> locations = new ArrayList<>();
        ArrayList<Integer> periodsOfTime = new ArrayList<>();
        ArrayList<Date> startDates = new ArrayList<>();
        ArrayList<Date> endDates = new ArrayList<>();
        ArrayList<Date> startSubDates = new ArrayList<>();
        ArrayList<Date> endSubDates = new ArrayList<>();
        ArrayList<Integer> maxPlayers = new ArrayList<>();
        ArrayList<Integer> maxTeamDims = new ArrayList<>();
        try {
            controller.getControllerHackathon().controllerGetAvailableHackathons(titles, locations, periodsOfTime, startDates, endDates,
                    startSubDates, endSubDates, maxPlayers, maxTeamDims, false);
        } catch (SQLException e) {
            String error = e.getMessage();
            int idx = error.indexOf("\n");
            error = error.substring(0, idx);
            JOptionPane.showMessageDialog(frame,
                    "C'è stato un errore!\n" + error,
                    "Errore", JOptionPane.ERROR_MESSAGE);
        }

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(new Color(240, 240, 245));
        listPanel.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));

        listPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        listPanel.setMaximumSize(new Dimension(700, listPanel.getPreferredSize().height));

        hackathonGroup = new ButtonGroup();
        for (int i = 0; i < titles.size(); i++) {
            JPanel card = createHackathonCard(titles.get(i), locations.get(i), periodsOfTime.get(i),
                    startDates.get(i), endDates.get(i), startSubDates.get(i), endSubDates.get(i),
                    maxPlayers.get(i), maxTeamDims.get(i));
            listPanel.add(card);
            listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ====== PANEL INFERIORE ======
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(240, 240, 245));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20));

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

        // Bottone "Registrati" al centro
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setBackground(new Color(240, 240, 245));

        JButton registerBtn = new JButton("Registrati");
        registerBtn.setPreferredSize(new Dimension(120, 35));
        registerBtn.setBackground(new Color(60, 179, 113)); // Verde
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFocusPainted(false);
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerBtn.addActionListener(e -> {
            String selectedHackathon = getSelectedHackathon();
            if (selectedHackathon == null) {
                JOptionPane.showMessageDialog(frame, "Seleziona un hackathon!", "Errore", JOptionPane.ERROR_MESSAGE);
            } else {
                int response = JOptionPane.showConfirmDialog(
                        frame,
                        "Sei sicuro di volerti registrare all'hackathon:\n" + selectedHackathon + "?",
                        "Conferma Registrazione",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                if (response == JOptionPane.YES_OPTION) {
                    try {
                        controller.getControllerPlayer().subscribe(controller.getUser().getUsername(), selectedHackathon,
                                locations.get(titles.indexOf(selectedHackathon)));
                        JOptionPane.showMessageDialog(frame,
                                "Registrazione completata con successo!\nHackathon: " + selectedHackathon,
                                "Registrazione Effettuata",
                                JOptionPane.INFORMATION_MESSAGE);
                        frame.dispose();
                        callerFrame.setVisible(true);
                    } catch (SQLException ex) {
                        String error = ex.getMessage();
                        int idx = error.indexOf("\n");
                        error = error.substring(0, idx);
                        JOptionPane.showMessageDialog(frame,
                                "C'è stato un errore!\n" + error,
                                "Registrazione Fallita", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        centerPanel.add(registerBtn);
        bottomPanel.add(centerPanel, BorderLayout.CENTER);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    // ====== CREA CARD HACKATHON ======
    private JPanel createHackathonCard(String titolo, String sede, int durata, Date dataInizio, Date dataFine,
                                       Date dataAperturaIscrizioni, Date dataChiusuraIscrizioni, int maxIscritti, int maxDimTeam) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(650, 60));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        JRadioButton radio = new JRadioButton(titolo);
        radio.setBackground(Color.WHITE);
        radio.setFont(new Font("Arial", Font.PLAIN, 16));

        // Quando si seleziona, apre un popup con le info riepilogative
        radio.addActionListener(e -> showHackathonInfoPopup(titolo, sede, durata, dataInizio, dataFine, dataAperturaIscrizioni,
                dataChiusuraIscrizioni, maxIscritti, maxDimTeam));

        hackathonGroup.add(radio);
        card.add(radio, BorderLayout.CENTER);

        card.setMaximumSize(new Dimension(700, 60));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        return card;
    }

    // ====== MOSTRA POPUP RIEPILOGO HACKATHON ======
    private void showHackathonInfoPopup(String titolo, String sede, int durata, Date dataInizio, Date dataFine,
                                        Date dataAperturaIscrizioni, Date dataChiusuraIscrizioni, int maxIscritti, int maxDimTeam) {
        JDialog dialog = new JDialog(frame, "Dettagli Hackathon", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(frame);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[][] data = {
                {"Titolo", titolo},
                {"Sede", sede},
                {"Durata", String.valueOf(durata)},
                {"Data Inizio", dataInizio.toString()},
                {"Data Fine", dataFine.toString()},
                {"Apertura Iscrizioni", dataAperturaIscrizioni.toString()},
                {"Chiusura Iscrizioni", dataChiusuraIscrizioni.toString()},
                {"Max Iscritti", String.valueOf(maxIscritti)},
                {"Max Dimensione Team", String.valueOf(maxDimTeam)}
        };

        String[] columnNames = {"Tipo", "Informazione"};
        JTable table = new JTable(data, columnNames);
        table.setEnabled(false);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Riepilogo Hackathon"));
        panel.add(tableScroll, BorderLayout.CENTER);

        JButton closeBtn = new JButton("Chiudi");
        closeBtn.setBackground(new Color(150, 150, 150));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setPreferredSize(new Dimension(100, 30));
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(e -> dialog.dispose());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(new Color(245, 245, 250));
        btnPanel.add(closeBtn);

        panel.add(btnPanel, BorderLayout.SOUTH);

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    // ====== OTTIENI HACKATHON SELEZIONATO ======
    private String getSelectedHackathon() {
        for (Enumeration<AbstractButton> buttons = hackathonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) return button.getText();
        }
        return null;
    }
}
