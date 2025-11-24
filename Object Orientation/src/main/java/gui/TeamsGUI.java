package gui;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.*;
import controller.*;

/**
 * Interfaccia grafica per il giudice che gestisce la visualizzazione
 * dei team partecipanti a un determinato hackathon.
 * Consente al giudice di visualizzare i team registrati,
 * selezionarne uno tramite {@link JRadioButton} e accedere alla schermata successiva
 * per esaminare e valutare il team scelto.
 */
public class TeamsGUI {
    private JFrame frame;
    private JPanel mainPanel;
    private ButtonGroup teamGroup;

    /**
     * Inizializza la finestra, recupera l'elenco dei team associati a un hackathon
     * e costruisce l'interfaccia grafica per permettere la selezione di un team.
     *
     * @param controller        il controller principale
     * @param callerFrame       il frame chiamante, da cui si accede a questa GUI
     * @param selectedHackathon il nome dell’hackathon selezionato
     * @param location          la sede dell’hackathon
     */
    public TeamsGUI(Controller controller, JFrame callerFrame, String selectedHackathon, String location) {
        frame = new JFrame("Seleziona Team");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 245));

        // ====== HEADER ======
        JLabel titleLabel = new JLabel("Seleziona Team", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // ====== DATI TEAM ======
        ArrayList<String> teamNames = new ArrayList<>();
        try {
            controller.getControllerJudge().controllerGetTeams(selectedHackathon, location, teamNames, false);
        } catch (SQLException e) {
            String error = e.getMessage();
            int idx = error.indexOf("\n");
            error = error.substring(0, idx);
            JOptionPane.showMessageDialog(frame,
                    "C'è stato un errore!\n" + error,
                    "Errore", JOptionPane.ERROR_MESSAGE);
        }

        // ====== LISTA TEAM CON RADIOBUTTON ======
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(new Color(240, 240, 245));
        listPanel.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));

        teamGroup = new ButtonGroup();
        for (String team : teamNames) {
            JPanel card = createTeamCard(team);
            listPanel.add(card);
            listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        // Forzo il preferred size del listPanel in base al numero di elementi
        int cardHeight = 50;
        int gap = 10;
        int totalHeight = teamNames.size() * (cardHeight + gap) + 20;
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

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setBackground(new Color(240, 240, 245));

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
                new ExAndVoteGUI(controller, frame, selectedTeam, selectedHackathon, location);
            }
        });

        centerPanel.add(openBtn);
        bottomPanel.add(centerPanel, BorderLayout.CENTER);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    /**
     * Crea una card per un singolo team,
     * contenente un {@link JRadioButton} per la selezione.
     *
     * @param teamName il nome del team da visualizzare nella card
     * @return un JPanel contenente il nome del team e il relativo JRadioButton
     */
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

    /**
     * Restituisce il nome del team selezionato dall’utente nella lista dei {@link JRadioButton}
     *
     * @return il nome del team selezionato, oppure {@code null} se nessun team è stato selezionato
     */
    private String getSelectedTeam() {
        for (Enumeration<AbstractButton> buttons = teamGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) return button.getText();
        }
        return null;
    }
}