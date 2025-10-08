package gui;

import java.awt.*;
import java.util.Enumeration;
import javax.swing.*;
import controller.*;

public class PlayerGUI {
    private JFrame frame;
    private JPanel mainPanel;
    private ButtonGroup teamGroup;

    public PlayerGUI(Controller controller, JFrame callerFrame) {
        frame = new JFrame("Partecipa a Team");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 245));

        // ====== HEADER ======
        JLabel titleLabel = new JLabel("Seleziona Team a cui Partecipare", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // ====== LISTA TEAM CON RADIOBUTTON ======
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(new Color(240, 240, 245));
        listPanel.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));

        // Lista di team disponibili
        String[] teamList = {
                "Team Alpha",
                "Team Beta",
                "Team Gamma",
                "Team Delta",
                "Team Epsilon",
                "Team Omega",
                "Team Innovatori"
        };

        teamGroup = new ButtonGroup();
        for (String team : teamList) {
            JPanel card = createTeamCard(team);
            listPanel.add(card);
            listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        // Forza il preferred size del listPanel in base al numero di elementi
        int cardHeight = 50;
        int gap = 10;
        int totalHeight = teamList.length * (cardHeight + gap) + 20;
        listPanel.setPreferredSize(new Dimension(600, Math.max(totalHeight, 300)));

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
            callerFrame.setVisible(true); // torna alla GUI chiamante
        });
        leftPanel.add(backBtn);
        bottomPanel.add(leftPanel, BorderLayout.WEST);

        // Pannello centrale con i due pulsanti
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        centerPanel.setBackground(new Color(240, 240, 245));

        // Bottone "Cambia"
        JButton changeBtn = new JButton("Cambia");
        changeBtn.setPreferredSize(new Dimension(120, 35));
        changeBtn.setBackground(new Color(220, 120, 60)); // Colore arancione
        changeBtn.setForeground(Color.WHITE);
        changeBtn.setFocusPainted(false);
        changeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        changeBtn.addActionListener(e -> {
            String selectedTeam = getSelectedTeam();
            if (selectedTeam == null) {
                JOptionPane.showMessageDialog(frame, "Seleziona un team!", "Errore", JOptionPane.ERROR_MESSAGE);
            } else {
                frame.setVisible(false);
                new JoinGUI(controller, frame, selectedTeam); // Apre JoinGUI
            }
        });

        // Bottone "Partecipa"
        JButton openBtn = new JButton("Apri");
        openBtn.setPreferredSize(new Dimension(120, 35));
        openBtn.setBackground(new Color(70, 130, 180));
        openBtn.setForeground(Color.WHITE);
        openBtn.setFocusPainted(false);
        openBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        openBtn.addActionListener(e -> {
            String selectedTeam = getSelectedTeam();
            if (selectedTeam == null) {
                JOptionPane.showMessageDialog(frame, "Seleziona un team!", "Errore", JOptionPane.ERROR_MESSAGE);
            } else {
                frame.setVisible(false);
                new MyTeamGUI(controller, frame, selectedTeam); // Apre MyTeamGUI
            }
        });

        centerPanel.add(changeBtn);
        centerPanel.add(openBtn);
        bottomPanel.add(centerPanel, BorderLayout.CENTER);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    // ====== CREAZIONE CARD TEAM ======
    private JPanel createTeamCard(String teamName) {
        JPanel card = new JPanel(new BorderLayout());
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        JRadioButton radio = new JRadioButton(teamName);
        radio.setBackground(Color.WHITE);
        radio.setFont(new Font("Arial", Font.PLAIN, 16));
        teamGroup.add(radio);

        card.add(radio, BorderLayout.CENTER);

        return card;
    }

    // ====== OTTIENI TEAM SELEZIONATO ======
    private String getSelectedTeam() {
        for (Enumeration<AbstractButton> buttons = teamGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) return button.getText();
        }
        return null;
    }
}