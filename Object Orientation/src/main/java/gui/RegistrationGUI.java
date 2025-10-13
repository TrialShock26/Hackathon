package gui;

import java.awt.*;
import java.util.Enumeration;
import javax.swing.*;
import controller.*;

public class RegistrationGUI {
    private JFrame frame;
    private JPanel mainPanel;
    private ButtonGroup hackathonGroup;

    // ====== CLASSE DATI HACKATHON ======
    private static class HackathonInfo {
        String titolo;
        String sede;
        String durata;
        String dataInizio;
        String dataFine;
        String periodoIscrizioni;
        String dataAperturaIscrizioni;
        String dataChiusuraIscrizioni;
        int maxIscritti;
        int maxDimTeam;
        String descrizioneProblema;

        public HackathonInfo(String titolo, String sede, String durata, String dataInizio, String dataFine,
                             String periodoIscrizioni, String dataAperturaIscrizioni, String dataChiusuraIscrizioni,
                             int maxIscritti, int maxDimTeam, String descrizioneProblema) {
            this.titolo = titolo;
            this.sede = sede;
            this.durata = durata;
            this.dataInizio = dataInizio;
            this.dataFine = dataFine;
            this.periodoIscrizioni = periodoIscrizioni;
            this.dataAperturaIscrizioni = dataAperturaIscrizioni;
            this.dataChiusuraIscrizioni = dataChiusuraIscrizioni;
            this.maxIscritti = maxIscritti;
            this.maxDimTeam = maxDimTeam;
            this.descrizioneProblema = descrizioneProblema;
        }
    }

    public RegistrationGUI(Controller controller, JFrame callerFrame) {
        frame = new JFrame("Registrazione Hackathon");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 245));

        // ====== HEADER ======
        JLabel titleLabel = new JLabel("Registrati ad un Hackathon", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // ====== DATI HACKATHON ======
        java.util.Map<String, HackathonInfo> hackathonData = new java.util.LinkedHashMap<>();

        hackathonData.put("Hack4Future 2025", new HackathonInfo(
                "Hack4Future 2025", "Milano", "3 giorni",
                "12/04/2025", "14/04/2025",
                "dal 01/02/2025 al 10/04/2025",
                "01/02/2025", "10/04/2025",
                200, 5,
                "Sviluppa soluzioni digitali sostenibili per il futuro delle città."
        ));

        hackathonData.put("TechSprint 2025", new HackathonInfo(
                "TechSprint 2025", "Torino", "2 giorni",
                "22/05/2025", "23/05/2025",
                "dal 01/03/2025 al 20/05/2025",
                "01/03/2025", "20/05/2025",
                150, 4,
                "Crea prototipi innovativi in ambito intelligenza artificiale e machine learning."
        ));

        hackathonData.put("CodeWave 2025", new HackathonInfo(
                "CodeWave 2025", "Roma", "3 giorni",
                "10/06/2025", "12/06/2025",
                "dal 01/03/2025 al 08/06/2025",
                "01/03/2025", "08/06/2025",
                250, 5,
                "Sfida i tuoi limiti sviluppando applicazioni innovative per il benessere digitale."
        ));

        hackathonData.put("GreenTech Challenge", new HackathonInfo(
                "GreenTech Challenge", "Firenze", "4 giorni",
                "18/07/2025", "21/07/2025",
                "dal 01/04/2025 al 15/07/2025",
                "01/04/2025", "15/07/2025",
                300, 6,
                "Progetta soluzioni tecnologiche per la sostenibilità ambientale."
        ));

        hackathonData.put("AI Revolution 2025", new HackathonInfo(
                "AI Revolution 2025", "Napoli", "2 giorni",
                "05/09/2025", "06/09/2025",
                "dal 01/05/2025 al 01/09/2025",
                "01/05/2025", "01/09/2025",
                180, 4,
                "Crea un prototipo basato su intelligenza artificiale che migliori la vita quotidiana."
        ));


        // ====== LISTA HACKATHON CON RADIOBUTTON ======
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(new Color(240, 240, 245));
        listPanel.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));

        hackathonGroup = new ButtonGroup();
        for (String hackathon : hackathonData.keySet()) {
            HackathonInfo info = hackathonData.get(hackathon);
            JPanel card = createHackathonCard(info);
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
                    JOptionPane.showMessageDialog(frame,
                            "Registrazione completata con successo!\nHackathon: " + selectedHackathon,
                            "Registrazione Effettuata",
                            JOptionPane.INFORMATION_MESSAGE);

                    frame.dispose();
                    callerFrame.setVisible(true);
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
    private JPanel createHackathonCard(HackathonInfo info) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(650, 60));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        JRadioButton radio = new JRadioButton(info.titolo);
        radio.setBackground(Color.WHITE);
        radio.setFont(new Font("Arial", Font.PLAIN, 16));

        // Quando si seleziona, apre un popup con le info riepilogative
        radio.addActionListener(e -> showHackathonInfoPopup(info));

        hackathonGroup.add(radio);
        card.add(radio, BorderLayout.CENTER);

        return card;
    }

    // ====== MOSTRA POPUP RIEPILOGO HACKATHON ======
    private void showHackathonInfoPopup(HackathonInfo info) {
        JDialog dialog = new JDialog(frame, "Dettagli Hackathon", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(frame);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Tabella di informazioni
        String[][] data = {
                {"Titolo", info.titolo},
                {"Sede", info.sede},
                {"Durata", info.durata},
                {"Data Inizio", info.dataInizio},
                {"Data Fine", info.dataFine},
                {"Periodo Iscrizioni", info.periodoIscrizioni},
                {"Apertura Iscrizioni", info.dataAperturaIscrizioni},
                {"Chiusura Iscrizioni", info.dataChiusuraIscrizioni},
                {"Max Iscritti", String.valueOf(info.maxIscritti)},
                {"Max Dimensione Team", String.valueOf(info.maxDimTeam)}
        };

        String[] columnNames = {"Campo", "Valore"};
        JTable table = new JTable(data, columnNames);
        table.setEnabled(false);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Riepilogo Hackathon"));
        panel.add(tableScroll, BorderLayout.CENTER);

        // Bottone chiudi
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
